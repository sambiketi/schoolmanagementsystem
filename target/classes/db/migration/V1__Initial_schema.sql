-- V1__Initial_schema.sql - School Management System
-- Created: 2026-02-11 11:46:18
-- Default Password: sam123 (CHANGE IN PRODUCTION!)

-- PASTE YOUR COMPLETE SQL SCHEMA HERE

-- V1__Initial_schema.sql - School Management System
-- Created: 2026-02-11 11:46:18
-- Default Password: sam123 (CHANGE IN PRODUCTION!)
-- =====================================================

-- =====================================================
-- PART 1: EXTENSIONS
-- =====================================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

-- =====================================================
-- PART 2: USERS TABLE (CORE)
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    display_id VARCHAR(20) UNIQUE NOT NULL,
    display_id_prefix VARCHAR(3) NOT NULL CHECK (display_id_prefix IN ('STU', 'TEA', 'ADM')),
    display_id_year SMALLINT NOT NULL CHECK (display_id_year >= 2000 AND display_id_year <= 2100),
    display_id_sequence INT NOT NULL CHECK (display_id_sequence > 0),
    display_id_check CHAR(1) NOT NULL,
    auth_type VARCHAR(10) NOT NULL DEFAULT 'PASSWORD' CHECK (auth_type IN ('PASSWORD', 'ID_ONLY')),
    username VARCHAR(100) UNIQUE,
    email VARCHAR(255),
    password_hash VARCHAR(255),
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    phone_number VARCHAR(20),
    address TEXT,
    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('STUDENT', 'TEACHER', 'ADMIN')),
    role_subtype VARCHAR(50),
    department_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_locked BOOLEAN NOT NULL DEFAULT false,
    failed_login_attempts INT DEFAULT 0,
    must_change_password BOOLEAN DEFAULT false,
    last_login TIMESTAMP,
    last_password_change TIMESTAMP,
    login_count INT DEFAULT 0,
    current_session_id VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT chk_student_auth CHECK (
        (user_type = 'STUDENT' AND auth_type = 'ID_ONLY' AND username IS NULL AND password_hash IS NULL) OR
        (user_type != 'STUDENT' AND auth_type = 'PASSWORD' AND username IS NOT NULL)
    ),
    CONSTRAINT chk_email_format CHECK (email IS NULL OR email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- =====================================================
-- PART 3: DEPARTMENTS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    can_create_spreadsheets BOOLEAN DEFAULT true,
    can_submit_reports BOOLEAN DEFAULT true,
    max_spreadsheets INT DEFAULT 10,
    data_retention_days INT DEFAULT 365,
    department_head_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT true
);

-- =====================================================
-- PART 4: DEPARTMENT SPREADSHEETS (DYNAMIC TABLES)
-- =====================================================
CREATE TABLE IF NOT EXISTS department_spreadsheets (
    id BIGSERIAL PRIMARY KEY,
    department_id BIGINT NOT NULL,
    spreadsheet_name VARCHAR(100) NOT NULL,
    description TEXT,
    columns_json JSONB NOT NULL,
    can_edit JSONB DEFAULT '["DEPARTMENT_ADMIN"]',
    can_view JSONB DEFAULT '["DEPARTMENT_ADMIN", "TEACHER"]',
    version INT DEFAULT 1,
    previous_version_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_at TIMESTAMP,
    updated_by BIGINT,
    is_active BOOLEAN DEFAULT true,
    CONSTRAINT unique_department_spreadsheet UNIQUE(department_id, spreadsheet_name)
);

-- =====================================================
-- PART 5: SPREADSHEET DATA (ACTUAL DATA STORAGE)
-- =====================================================
CREATE TABLE IF NOT EXISTS spreadsheet_data (
    id BIGSERIAL PRIMARY KEY,
    spreadsheet_id BIGINT NOT NULL,
    row_data JSONB NOT NULL,
    row_hash VARCHAR(64) NOT NULL,
    row_status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (row_status IN ('ACTIVE', 'ARCHIVED', 'DELETED')),
    version INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_at TIMESTAMP,
    updated_by BIGINT
);

-- =====================================================
-- PART 6: STUDENTS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS students (
    user_id BIGINT PRIMARY KEY,
    admission_date DATE NOT NULL,
    admission_year SMALLINT NOT NULL,
    current_class VARCHAR(50),
    class_teacher_id BIGINT,
    parent_guardian_name VARCHAR(255),
    parent_relationship VARCHAR(50),
    parent_phone VARCHAR(20),
    parent_email VARCHAR(255),
    parent_occupation VARCHAR(100),
    emergency_contact_name VARCHAR(255),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relationship VARCHAR(50),
    medical_conditions TEXT,
    allergies TEXT,
    blood_group VARCHAR(5),
    preferred_hospital VARCHAR(255),
    previous_school VARCHAR(255),
    transfer_certificate_number VARCHAR(100),
    last_class_passed VARCHAR(50),
    student_status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (
        student_status IN ('ACTIVE', 'TRANSFERRED', 'GRADUATED', 'SUSPENDED', 'EXPIRED')
    ),
    exit_date DATE,
    exit_reason TEXT
);

-- =====================================================
-- PART 7: TEACHERS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS teachers (
    user_id BIGINT PRIMARY KEY,
    employee_number VARCHAR(50) UNIQUE,
    hire_date DATE NOT NULL,
    employment_type VARCHAR(20) NOT NULL CHECK (
        employment_type IN ('PERMANENT', 'CONTRACT', 'PART_TIME', 'VISITING')
    ),
    contract_end_date DATE,
    salary_grade VARCHAR(10),
    qualification_summary TEXT,
    subjects_certified JSONB,
    classes_assigned JSONB,
    department_affiliations JSONB,
    official_email VARCHAR(255),
    office_extension VARCHAR(10),
    office_room VARCHAR(20),
    last_training_date DATE,
    next_training_date DATE,
    training_certifications JSONB,
    teaching_hours_per_week INT DEFAULT 0,
    administrative_hours_per_week INT DEFAULT 0,
    employment_status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (
        employment_status IN ('ACTIVE', 'ON_LEAVE', 'SUSPENDED', 'RESIGNED', 'RETIRED')
    ),
    leave_balance_days INT DEFAULT 30
);

-- =====================================================
-- PART 8: ADMINISTRATORS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS administrators (
    user_id BIGINT PRIMARY KEY,
    admin_role VARCHAR(50) NOT NULL,
    department_id BIGINT,
    can_manage_users BOOLEAN DEFAULT false,
    can_manage_departments BOOLEAN DEFAULT false,
    can_view_all_data BOOLEAN DEFAULT false,
    can_generate_reports BOOLEAN DEFAULT false,
    can_manage_backups BOOLEAN DEFAULT false,
    office_number VARCHAR(50),
    official_title VARCHAR(100),
    extension VARCHAR(10),
    digital_signature_path VARCHAR(255),
    signature_authorized_date DATE
);

-- =====================================================
-- PART 9: CLASSES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS classes (
    id BIGSERIAL PRIMARY KEY,
    class_code VARCHAR(20) UNIQUE NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    academic_year SMALLINT NOT NULL,
    class_teacher_id BIGINT,
    assistant_teacher_id BIGINT,
    max_students INT DEFAULT 40,
    current_students_count INT DEFAULT 0,
    classroom_number VARCHAR(20),
    building VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT
);

-- =====================================================
-- PART 10: SUBJECT GRADES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS subject_grades (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    teacher_id BIGINT NOT NULL,
    class_teacher_id BIGINT,
    academic_year SMALLINT NOT NULL,
    term_number INT CHECK (term_number BETWEEN 1 AND 3),
    grading_date DATE NOT NULL,
    grade_value VARCHAR(10),
    grade_type VARCHAR(20) DEFAULT 'LETTER' CHECK (grade_type IN ('LETTER', 'PERCENTAGE', 'DESCRIPTIVE')),
    max_possible_value VARCHAR(10),
    class_rank INT,
    total_in_class INT,
    teacher_comments TEXT,
    class_teacher_comments TEXT,
    submission_status VARCHAR(20) DEFAULT 'DRAFT' CHECK (
        submission_status IN ('DRAFT', 'SUBMITTED', 'REVIEWED', 'APPROVED', 'REJECTED')
    ),
    forwarded_to_class_teacher BOOLEAN DEFAULT false,
    class_teacher_reviewed BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT unique_student_subject_term UNIQUE(student_id, subject_name, academic_year, term_number)
);

-- =====================================================
-- PART 11: REPORTS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS reports (
    id BIGSERIAL PRIMARY KEY,
    report_type VARCHAR(50) NOT NULL CHECK (
        report_type IN ('TEACHER_REPORT', 'DEPT_REPORT', 'REQUEST', 'INCIDENT', 'PERFORMANCE')
    ),
    title VARCHAR(255) NOT NULL,
    from_user_id BIGINT NOT NULL,
    from_department_id BIGINT,
    to_user_id BIGINT,
    to_department_id BIGINT,
    content TEXT NOT NULL,
    attachments_json JSONB,
    status VARCHAR(20) DEFAULT 'DRAFT' CHECK (
        status IN ('DRAFT', 'SUBMITTED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'ARCHIVED')
    ),
    priority VARCHAR(10) DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    submission_date DATE,
    deadline_date DATE,
    review_date DATE,
    archived_date DATE,
    reviewed_by BIGINT,
    review_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- =====================================================
-- PART 12: LOGIN LOGS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS login_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    display_id VARCHAR(20),
    user_type VARCHAR(20),
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    logout_time TIMESTAMP,
    session_duration INTERVAL GENERATED ALWAYS AS (logout_time - login_time) STORED,
    ip_address INET,
    mac_address VARCHAR(17),
    user_agent TEXT,
    computer_name VARCHAR(100),
    location VARCHAR(100),
    login_status VARCHAR(20) NOT NULL CHECK (
        login_status IN ('SUCCESS', 'FAILED_ID', 'FAILED_PASSWORD', 'LOCKED', 'EXPIRED')
    ),
    logout_reason VARCHAR(50) CHECK (
        logout_reason IN ('USER', 'TIMEOUT', 'ADMIN', 'END_OF_DAY', 'SYSTEM')
    ),
    is_suspicious BOOLEAN DEFAULT false,
    suspicious_reason TEXT
);

-- =====================================================
-- PART 13: BACKUP LOGS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS backup_logs (
    id BIGSERIAL PRIMARY KEY,
    backup_type VARCHAR(20) NOT NULL CHECK (
        backup_type IN ('FULL', 'INCREMENTAL', 'DEPARTMENT', 'EMERGENCY')
    ),
    backup_name VARCHAR(255) NOT NULL,
    departments_included JSONB,
    tables_included JSONB,
    storage_path VARCHAR(500) NOT NULL,
    backup_size_bytes BIGINT,
    compression_ratio DECIMAL(5,2),
    checksum VARCHAR(64) NOT NULL,
    verification_status VARCHAR(20) DEFAULT 'PENDING' CHECK (
        verification_status IN ('PENDING', 'VERIFIED', 'FAILED', 'CORRUPT')
    ),
    verified_at TIMESTAMP,
    retention_days INT NOT NULL,
    scheduled_deletion_date DATE GENERATED ALWAYS AS (backup_date + retention_days * INTERVAL '1 day') STORED,
    backup_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    duration INTERVAL GENERATED ALWAYS AS (completed_at - started_at) STORED,
    status VARCHAR(20) NOT NULL CHECK (
        status IN ('STARTED', 'COMPLETED', 'FAILED', 'CANCELLED')
    ),
    error_message TEXT,
    initiated_by BIGINT,
    initiated_type VARCHAR(20) CHECK (initiated_type IN ('AUTOMATED', 'MANUAL'))
);

-- =====================================================
-- PART 14: SYSTEM CONFIG TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS system_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value JSONB NOT NULL,
    config_type VARCHAR(50) NOT NULL CHECK (
        config_type IN ('STRING', 'NUMBER', 'BOOLEAN', 'JSON', 'ARRAY')
    ),
    config_scope VARCHAR(50) DEFAULT 'GLOBAL' CHECK (
        config_scope IN ('GLOBAL', 'DEPARTMENT', 'USER', 'CLASS')
    ),
    scope_id BIGINT,
    is_encrypted BOOLEAN DEFAULT false,
    encryption_key_id VARCHAR(100),
    version INT DEFAULT 1,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    updated_by BIGINT
);

-- =====================================================
-- PART 15: FOREIGN KEY CONSTRAINTS
-- =====================================================
ALTER TABLE users ADD CONSTRAINT fk_users_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE users ADD CONSTRAINT fk_users_updated_by FOREIGN KEY (updated_by) REFERENCES users(id);
ALTER TABLE departments ADD CONSTRAINT fk_departments_head FOREIGN KEY (department_head_id) REFERENCES users(id);
ALTER TABLE departments ADD CONSTRAINT fk_departments_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE department_spreadsheets ADD CONSTRAINT fk_spreadsheets_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE;
ALTER TABLE department_spreadsheets ADD CONSTRAINT fk_spreadsheets_previous FOREIGN KEY (previous_version_id) REFERENCES department_spreadsheets(id);
ALTER TABLE department_spreadsheets ADD CONSTRAINT fk_spreadsheets_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE department_spreadsheets ADD CONSTRAINT fk_spreadsheets_updated_by FOREIGN KEY (updated_by) REFERENCES users(id);
ALTER TABLE spreadsheet_data ADD CONSTRAINT fk_data_spreadsheet FOREIGN KEY (spreadsheet_id) REFERENCES department_spreadsheets(id) ON DELETE CASCADE;
ALTER TABLE spreadsheet_data ADD CONSTRAINT fk_data_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE spreadsheet_data ADD CONSTRAINT fk_data_updated_by FOREIGN KEY (updated_by) REFERENCES users(id);
ALTER TABLE students ADD CONSTRAINT fk_students_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE students ADD CONSTRAINT fk_students_class_teacher FOREIGN KEY (class_teacher_id) REFERENCES users(id);
ALTER TABLE teachers ADD CONSTRAINT fk_teachers_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE administrators ADD CONSTRAINT fk_admins_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE administrators ADD CONSTRAINT fk_admins_department FOREIGN KEY (department_id) REFERENCES departments(id);
ALTER TABLE classes ADD CONSTRAINT fk_classes_teacher FOREIGN KEY (class_teacher_id) REFERENCES users(id);
ALTER TABLE classes ADD CONSTRAINT fk_classes_assistant FOREIGN KEY (assistant_teacher_id) REFERENCES users(id);
ALTER TABLE classes ADD CONSTRAINT fk_classes_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE subject_grades ADD CONSTRAINT fk_grades_student FOREIGN KEY (student_id) REFERENCES users(id);
ALTER TABLE subject_grades ADD CONSTRAINT fk_grades_teacher FOREIGN KEY (teacher_id) REFERENCES users(id);
ALTER TABLE subject_grades ADD CONSTRAINT fk_grades_class_teacher FOREIGN KEY (class_teacher_id) REFERENCES users(id);
ALTER TABLE subject_grades ADD CONSTRAINT fk_grades_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE subject_grades ADD CONSTRAINT fk_grades_updated_by FOREIGN KEY (updated_by) REFERENCES users(id);
ALTER TABLE reports ADD CONSTRAINT fk_reports_from_user FOREIGN KEY (from_user_id) REFERENCES users(id);
ALTER TABLE reports ADD CONSTRAINT fk_reports_from_dept FOREIGN KEY (from_department_id) REFERENCES departments(id);
ALTER TABLE reports ADD CONSTRAINT fk_reports_to_user FOREIGN KEY (to_user_id) REFERENCES users(id);
ALTER TABLE reports ADD CONSTRAINT fk_reports_to_dept FOREIGN KEY (to_department_id) REFERENCES departments(id);
ALTER TABLE reports ADD CONSTRAINT fk_reports_reviewed_by FOREIGN KEY (reviewed_by) REFERENCES users(id);
ALTER TABLE login_logs ADD CONSTRAINT fk_logs_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE backup_logs ADD CONSTRAINT fk_backup_initiated_by FOREIGN KEY (initiated_by) REFERENCES users(id);
ALTER TABLE system_config ADD CONSTRAINT fk_config_created_by FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE system_config ADD CONSTRAINT fk_config_updated_by FOREIGN KEY (updated_by) REFERENCES users(id);

-- =====================================================
-- PART 16: INDEXES
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_users_display_id ON users(display_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username) WHERE username IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_users_user_type ON users(user_type);
CREATE INDEX IF NOT EXISTS idx_users_department ON users(department_id);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS idx_departments_code ON departments(code);
CREATE INDEX IF NOT EXISTS idx_departments_head ON departments(department_head_id);
CREATE INDEX IF NOT EXISTS idx_spreadsheets_department ON department_spreadsheets(department_id);
CREATE INDEX IF NOT EXISTS idx_spreadsheets_active ON department_spreadsheets(is_active);
CREATE INDEX IF NOT EXISTS idx_spreadsheet_data_spreadsheet ON spreadsheet_data(spreadsheet_id);
CREATE INDEX IF NOT EXISTS idx_spreadsheet_data_created ON spreadsheet_data(created_at);
CREATE INDEX IF NOT EXISTS idx_spreadsheet_data_status ON spreadsheet_data(row_status);
CREATE INDEX IF NOT EXISTS idx_spreadsheet_data_json ON spreadsheet_data USING GIN (row_data);
CREATE INDEX IF NOT EXISTS idx_students_class ON students(current_class);
CREATE INDEX IF NOT EXISTS idx_students_admission_year ON students(admission_year);
CREATE INDEX IF NOT EXISTS idx_students_status ON students(student_status);
CREATE INDEX IF NOT EXISTS idx_teachers_employee_number ON teachers(employee_number);
CREATE INDEX IF NOT EXISTS idx_teachers_employment_type ON teachers(employment_type);
CREATE INDEX IF NOT EXISTS idx_teachers_hire_date ON teachers(hire_date);
CREATE INDEX IF NOT EXISTS idx_teachers_status ON teachers(employment_status);
CREATE INDEX IF NOT EXISTS idx_admins_role ON administrators(admin_role);
CREATE INDEX IF NOT EXISTS idx_admins_department ON administrators(department_id);
CREATE INDEX IF NOT EXISTS idx_classes_academic_year ON classes(academic_year);
CREATE INDEX IF NOT EXISTS idx_classes_teacher ON classes(class_teacher_id);
CREATE INDEX IF NOT EXISTS idx_classes_active ON classes(is_active);
CREATE INDEX IF NOT EXISTS idx_grades_student ON subject_grades(student_id);
CREATE INDEX IF NOT EXISTS idx_grades_teacher ON subject_grades(teacher_id);
CREATE INDEX IF NOT EXISTS idx_grades_academic_year ON subject_grades(academic_year, term_number);
CREATE INDEX IF NOT EXISTS idx_grades_status ON subject_grades(submission_status);
CREATE INDEX IF NOT EXISTS idx_reports_from_user ON reports(from_user_id);
CREATE INDEX IF NOT EXISTS idx_reports_to_user ON reports(to_user_id);
CREATE INDEX IF NOT EXISTS idx_reports_status ON reports(status);
CREATE INDEX IF NOT EXISTS idx_reports_deadline ON reports(deadline_date);
CREATE INDEX IF NOT EXISTS idx_reports_created ON reports(created_at);
CREATE INDEX IF NOT EXISTS idx_login_logs_user ON login_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_login_logs_time ON login_logs(login_time);
CREATE INDEX IF NOT EXISTS idx_login_logs_status ON login_logs(login_status);
CREATE INDEX IF NOT EXISTS idx_login_logs_ip ON login_logs(ip_address);
CREATE INDEX IF NOT EXISTS idx_backup_logs_date ON backup_logs(backup_date);
CREATE INDEX IF NOT EXISTS idx_backup_logs_type ON backup_logs(backup_type);
CREATE INDEX IF NOT EXISTS idx_backup_logs_status ON backup_logs(status);
CREATE INDEX IF NOT EXISTS idx_backup_logs_deletion ON backup_logs(scheduled_deletion_date);
CREATE INDEX IF NOT EXISTS idx_config_key ON system_config(config_key);
CREATE INDEX IF NOT EXISTS idx_config_scope ON system_config(config_scope, scope_id);

-- =====================================================
-- PART 17: FUNCTIONS AND TRIGGERS
-- =====================================================

-- Function: Generate display_id check digit
CREATE OR REPLACE FUNCTION generate_display_id_check()
RETURNS TRIGGER AS $$
BEGIN
    NEW.display_id_check := CHR(ABS(('x' || substr(md5(NEW.display_id_prefix || '-' || NEW.display_id_year || '-' || NEW.display_id_sequence), 1, 8))::bit(32)::int % 26 + 65));
    NEW.display_id := NEW.display_id_prefix || '/' || 
                      RIGHT(NEW.display_id_year::TEXT, 2) || '/' || 
                      LPAD(NEW.display_id_sequence::TEXT, 4, '0') || '/' || 
                      NEW.display_id_check;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Auto-generate display_id
DROP TRIGGER IF EXISTS trg_users_generate_display_id ON users;
CREATE TRIGGER trg_users_generate_display_id
    BEFORE INSERT ON users
    FOR EACH ROW
    WHEN (NEW.display_id IS NULL)
    EXECUTE FUNCTION generate_display_id_check();

-- Function: Update timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Update timestamp triggers
DROP TRIGGER IF EXISTS trg_users_updated_at ON users;
CREATE TRIGGER trg_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
DROP TRIGGER IF EXISTS trg_departments_updated_at ON departments;
CREATE TRIGGER trg_departments_updated_at BEFORE UPDATE ON departments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
DROP TRIGGER IF EXISTS trg_spreadsheets_updated_at ON department_spreadsheets;
CREATE TRIGGER trg_spreadsheets_updated_at BEFORE UPDATE ON department_spreadsheets FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
DROP TRIGGER IF EXISTS trg_spreadsheet_data_updated_at ON spreadsheet_data;
CREATE TRIGGER trg_spreadsheet_data_updated_at BEFORE UPDATE ON spreadsheet_data FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
DROP TRIGGER IF EXISTS trg_grades_updated_at ON subject_grades;
CREATE TRIGGER trg_grades_updated_at BEFORE UPDATE ON subject_grades FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Function: Calculate row hash
CREATE OR REPLACE FUNCTION calculate_row_hash()
RETURNS TRIGGER AS $$
BEGIN
    NEW.row_hash := ENCODE(DIGEST(NEW.row_data::TEXT, 'sha256'), 'hex');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Auto-calculate hash
DROP TRIGGER IF EXISTS trg_spreadsheet_data_hash ON spreadsheet_data;
CREATE TRIGGER trg_spreadsheet_data_hash
    BEFORE INSERT OR UPDATE ON spreadsheet_data
    FOR EACH ROW
    EXECUTE FUNCTION calculate_row_hash();

-- =====================================================
-- PART 18: INITIAL SEED DATA
-- =====================================================

-- Insert default admin user (Principal)
INSERT INTO users (display_id_prefix, display_id_year, display_id_sequence, display_id_check, 
                   auth_type, username, password_hash, full_name, user_type, role_subtype, is_active)
SELECT 'ADM', 2025, 1, 'X', 'PASSWORD', 'principal', 
        crypt('sam123', gen_salt('bf')), 'System Principal', 'ADMIN', 'PRINCIPAL', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'principal');

-- Insert default departments
INSERT INTO departments (code, name, description, created_by)
SELECT 'PRIN', 'Principal''s Office', 'School administration and governance', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'PRIN');

INSERT INTO departments (code, name, description, created_by)
SELECT 'CURR', 'Curriculum Department', 'Academic programs and curriculum development', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'CURR');

INSERT INTO departments (code, name, description, created_by)
SELECT 'BORD', 'Boarding Department', 'Hostel and boarding facilities management', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'BORD');

INSERT INTO departments (code, name, description, created_by)
SELECT 'GAME', 'Games & Sports', 'Sports activities and competitions', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'GAME');

INSERT INTO departments (code, name, description, created_by)
SELECT 'DINE', 'Dining Services', 'Cafeteria and food services', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'DINE');

INSERT INTO departments (code, name, description, created_by)
SELECT 'DISC', 'Discipline Committee', 'Student conduct and discipline', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'DISC');

INSERT INTO departments (code, name, description, created_by)
SELECT 'LAB', 'Laboratories', 'Science and computer labs', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'LAB');

INSERT INTO departments (code, name, description, created_by)
SELECT 'LIBR', 'Library', 'Library and resource center', 1
WHERE NOT EXISTS (SELECT 1 FROM departments WHERE code = 'LIBR');

-- Insert system configuration
INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'system.name', '"School Management System"', 'STRING', 'System display name', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'system.name');

INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'academic.current_year', '2025', 'NUMBER', 'Current academic year', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'academic.current_year');

INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'security.max_login_attempts', '5', 'NUMBER', 'Maximum failed login attempts before lockout', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'security.max_login_attempts');

INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'security.session_timeout', '30', 'NUMBER', 'Session timeout in minutes', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'security.session_timeout');

INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'backup.default_retention', '30', 'NUMBER', 'Default backup retention in days', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'backup.default_retention');

INSERT INTO system_config (config_key, config_value, config_type, description, created_by)
SELECT 'school.name', '"St. John''s High School"', 'STRING', 'School name', 1
WHERE NOT EXISTS (SELECT 1 FROM system_config WHERE config_key = 'school.name');

-- Insert default admin permissions
INSERT INTO administrators (user_id, admin_role, department_id, can_manage_users, can_manage_departments, can_view_all_data, can_generate_reports, can_manage_backups)
SELECT 1, 'PRINCIPAL', (SELECT id FROM departments WHERE code = 'PRIN'), true, true, true, true, true
WHERE NOT EXISTS (SELECT 1 FROM administrators WHERE user_id = 1);

-- =====================================================
-- PART 19: GRANT PERMISSIONS
-- =====================================================

-- Create application user if not exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'school_admin') THEN
        CREATE USER school_admin WITH PASSWORD 'sam123';
    END IF;
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'school_reporting') THEN
        CREATE USER school_reporting WITH PASSWORD 'sam123';
    END IF;
END
$$;

-- Grant permissions
GRANT USAGE ON SCHEMA public TO school_admin;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO school_admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO school_admin;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO school_reporting;

-- =====================================================
-- MIGRATION COMPLETE
-- =====================================================
