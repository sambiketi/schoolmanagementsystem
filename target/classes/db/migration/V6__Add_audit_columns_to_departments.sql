-- Flyway migration script
-- Description: Add missing updated_at and updated_by columns to departments table

-- Add updated_at column if it doesn't exist
ALTER TABLE departments 
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add updated_by column if it doesn't exist - using BIGINT to match entity's Long type
ALTER TABLE departments 
ADD COLUMN IF NOT EXISTS updated_by BIGINT;

-- Backfill updated_at with created_at for existing records
UPDATE departments 
SET 
    updated_at = COALESCE(updated_at, created_at, CURRENT_TIMESTAMP),
    updated_by = COALESCE(updated_by, created_by, 0)
WHERE updated_at IS NULL OR updated_by IS NULL;

-- Create an index on updated_at for better query performance
CREATE INDEX IF NOT EXISTS idx_departments_updated_at ON departments(updated_at);

-- Create or replace the auto-update function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update updated_at on row updates
DROP TRIGGER IF EXISTS trigger_update_departments_updated_at ON departments;
CREATE TRIGGER trigger_update_departments_updated_at
    BEFORE UPDATE ON departments
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add comments for documentation
COMMENT ON COLUMN departments.updated_at IS 'Timestamp when the department record was last updated';
COMMENT ON COLUMN departments.updated_by IS 'ID of the user who last updated the record (matches entity Long type)';

-- Verify the migration
DO $$
BEGIN
    RAISE NOTICE 'Migration V6 completed: Added audit columns with correct BIGINT type for updated_by';
END $$;