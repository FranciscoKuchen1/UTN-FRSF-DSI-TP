## FAQs

### Q1: How do I resolve a "Port Already in Use" error?
- **Solution**: Stop the process running on the conflicting port by using:
  ```bash
  lsof -i :<port>
  ```
  Then kill the process:
  ```bash
  kill -9 <pid>
  ```

### Q2: What should I do if the application fails to connect to PostgreSQL?
- **Solution**:
  1. Verify that the PostgreSQL container is running.
  2. Check `application.yml` for the correct database credentials.
  3. Test connectivity using `psql`:
     ```bash
     psql -h localhost -p 5432 -U postgres -d project
     ```

### Q3: How do I reset the database?
- **Solution**: Use the following command to rebuild the database:
  ```bash
  docker compose down && docker compose up
  ```
