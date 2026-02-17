-- V5__Add_class_relationship.sql
-- Add class_id to students table and establish relationship

-- =====================================================
-- PART 1: ADD CLASS_ID COLUMN TO STUDENTS TABLE
-- =====================================================
ALTER TABLE students ADD COLUMN class_id BIGINT;

-- =====================================================
-- PART 2: ADD FOREIGN KEY CONSTRAINT
-- =====================================================
ALTER TABLE students ADD CONSTRAINT fk_students_class 
    FOREIGN KEY (class_id) REFERENCES classes(id);

-- =====================================================
-- PART 3: UPDATE EXISTING DATA
-- =====================================================
-- Map existing string class names to class entities
-- This is a sample mapping - you'll need to adjust based on your actual data

-- First, ensure we have basic classes for common grade levels
INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE1', 'Grade 1', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE1');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE2', 'Grade 2', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE2');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE3', 'Grade 3', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE3');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE4', 'Grade 4', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE4');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE5', 'Grade 5', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE5');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE6', 'Grade 6', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE6');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE7', 'Grade 7', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE7');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE8', 'Grade 8', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE8');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE9', 'Grade 9', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE9');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE10', 'Grade 10', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE10');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE11', 'Grade 11', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE11');

INSERT INTO classes (class_code, class_name, academic_year, max_students, is_active, created_by)
SELECT 'GRADE12', 'Grade 12', 2026, 40, true, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'GRADE12');

-- =====================================================
-- PART 4: MAP EXISTING STUDENTS TO CLASSES
-- =====================================================
-- Update students to point to appropriate classes based on current_class field
-- This is a simple mapping - you might need more complex logic for your data

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 1
    (s.current_class ILIKE '%grade 1%' OR s.current_class ILIKE '%grade1%' OR s.current_class = '1') 
    AND c.class_code = 'GRADE1'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 2
    (s.current_class ILIKE '%grade 2%' OR s.current_class ILIKE '%grade2%' OR s.current_class = '2') 
    AND c.class_code = 'GRADE2'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 3
    (s.current_class ILIKE '%grade 3%' OR s.current_class ILIKE '%grade3%' OR s.current_class = '3') 
    AND c.class_code = 'GRADE3'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 4
    (s.current_class ILIKE '%grade 4%' OR s.current_class ILIKE '%grade4%' OR s.current_class = '4') 
    AND c.class_code = 'GRADE4'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 5
    (s.current_class ILIKE '%grade 5%' OR s.current_class ILIKE '%grade5%' OR s.current_class = '5') 
    AND c.class_code = 'GRADE5'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 6
    (s.current_class ILIKE '%grade 6%' OR s.current_class ILIKE '%grade6%' OR s.current_class = '6') 
    AND c.class_code = 'GRADE6'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 7
    (s.current_class ILIKE '%grade 7%' OR s.current_class ILIKE '%grade7%' OR s.current_class = '7') 
    AND c.class_code = 'GRADE7'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 8
    (s.current_class ILIKE '%grade 8%' OR s.current_class ILIKE '%grade8%' OR s.current_class = '8') 
    AND c.class_code = 'GRADE8'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 9
    (s.current_class ILIKE '%grade 9%' OR s.current_class ILIKE '%grade9%' OR s.current_class = '9') 
    AND c.class_code = 'GRADE9'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 10
    (s.current_class ILIKE '%grade 10%' OR s.current_class ILIKE '%grade10%' OR s.current_class = '10') 
    AND c.class_code = 'GRADE10'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 11
    (s.current_class ILIKE '%grade 11%' OR s.current_class ILIKE '%grade11%' OR s.current_class = '11') 
    AND c.class_code = 'GRADE11'
    AND s.class_id IS NULL;

UPDATE students s
SET class_id = c.id
FROM classes c
WHERE 
    -- Match Grade 12
    (s.current_class ILIKE '%grade 12%' OR s.current_class ILIKE '%grade12%' OR s.current_class = '12') 
    AND c.class_code = 'GRADE12'
    AND s.class_id IS NULL;

-- =====================================================
-- PART 5: UPDATE CLASS STUDENT COUNTS
-- =====================================================
-- Update the current_students_count in classes table
UPDATE classes c
SET current_students_count = (
    SELECT COUNT(*) 
    FROM students s 
    WHERE s.class_id = c.id
);

-- =====================================================
-- PART 6: CREATE INDEX FOR PERFORMANCE
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_students_class_id ON students(class_id);

-- =====================================================
-- MIGRATION COMPLETE
-- =====================================================
SELECT '✅ Class relationship migration completed successfully' as message;