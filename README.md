Description:
This application enables the management of authors, books, and libraries using a PostgreSQL database and a Java Swing graphical interface. The application supports viewing, adding, editing, and deleting records and implements user authentication with two access levels: admin and client.

Technologies Used:
Java (Swing, JDBC)
PostgreSQL (database management)
Git, GitHub (version control and distribution)
Bcrypt (password security)
Maven (dependency management)

User authentication with role-based access (admin/client):
Author management (view, add, edit, delete - only for admin)
Book management (view, add, edit, delete - only for admin)
Library management (view, add, edit, delete - only for admin)
Many-to-Many relationship management between authors & books, books & libraries (including client restrictions)
Password security with bcrypt and authentication validation
Enhanced user interface with Java Swing
