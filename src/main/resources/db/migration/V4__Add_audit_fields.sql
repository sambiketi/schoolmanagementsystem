-- Add missing audit fields to teachers table
ALTER TABLE teachers 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Add missing audit fields to students table (if needed)
ALTER TABLE students 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- Add missing audit fields to administrators table (if needed)
ALTER TABLE administrators 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;