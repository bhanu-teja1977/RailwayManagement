-- Check if users exist in the database
USE railway_management;

SELECT 
    id, 
    name, 
    email, 
    role,
    SUBSTRING(password, 1, 20) as password_hash_start,
    created_at 
FROM users;

-- Count users
SELECT COUNT(*) as total_users FROM users;

-- Check if the specific emails exist
SELECT 
    CASE 
        WHEN EXISTS (SELECT 1 FROM users WHERE email = 'admin@railway.com') 
        THEN 'EXISTS' 
        ELSE 'NOT FOUND' 
    END as admin_status,
    CASE 
        WHEN EXISTS (SELECT 1 FROM users WHERE email = 'user@railway.com') 
        THEN 'EXISTS' 
        ELSE 'NOT FOUND' 
    END as user_status;
