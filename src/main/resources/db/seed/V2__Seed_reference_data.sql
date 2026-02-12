-- V2__Seed_reference_data.sql
-- School Management System - Reference Data
-- Created: 2026-02-11 12:46:28

-- =====================================================
-- THIS RUNS AFTER V1 SCHEMA IS CREATED
-- =====================================================

-- Add additional departments if needed
INSERT INTO departments (code, name, description, created_by)
SELECT 'IT', 'Information Technology', 'Computer systems and support', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'IT');

INSERT INTO departments (code, name, description, created_by)
SELECT 'TRANSPORT', 'Transport Services', 'School bus and vehicle management', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'TRANSPORT');

-- Add academic terms configuration
INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'academic.terms', '3', 'NUMBER', 'Number of terms per academic year', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'academic.terms');

INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'grading.pass_mark', '50', 'NUMBER', 'Minimum percentage to pass', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'grading.pass_mark');

-- Add sample class groups
INSERT INTO classes (class_code, class_name, academic_year, max_students, created_by)
SELECT 'FORM1A', 'Form 1 Alpha', 2026, 40, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'FORM1A');

INSERT INTO classes (class_code, class_name, academic_year, max_students, created_by)
SELECT 'FORM1B', 'Form 1 Bravo', 2026, 40, 1
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE class_code = 'FORM1B');

SELECT '✅ Seed data loaded successfully' as message;
