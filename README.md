# PIS PROJECT - HOTEL

## Set up & installation

1. Download IntelliJ (recommended)
2. Import project from gitlab
3. Download jdk17
4. Download and create database postgresql:

    - `CREATE DATABASE pis;`
    - `GRANT ALL PRIVILEGES ON DATABASE "pis" TO user_name;`
    - `GRANT ALL PRIVILEGES ON DATABASE "pis" TO postgres;`
5. Run the app
    - run `src/main/java/PisHotelApplication`
6. Test if works:
   `http://localhost:8080/api/user/`
   Expected result:

   ;;; {
   "firstName": "Karol",
   "lastName": "Fit",
   "dateOfBirth": "1998-06-22",
   "address": "Bozetechova 2, Brno, CZ",
   "idNumber": "E50241578",
   "phoneNumber": "00420412345656",
   "email": "karol.fit@vut.cz"
   }, {
   "firstName": "admin",
   "lastName": "admin",
   "dateOfBirth": "2000-06-20",
   "address": "Bozetechova 2, Brno, CZ",
   "idNumber": "E50241541",
   "phoneNumber": "00420659345656",
   "email": "admin.fit@vut.cz"
   } ;;;