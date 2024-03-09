supertest = require('supertest');
const assert = require('assert');
const app = require('../server.js'); // Assuming your server file is named server.js

let employee = readAllEmployees()

describe('API Tests', function() {
    let api;

    // Before each test, initialize Supertest with your Express app
    beforeEach(function() {
        api = supertest(app);
    });

    // Test GET /greeting
    it('GET /greeting should return "Hello world!"', function(done) {
        api.get('/greeting')
           .expect(200)
           .end(function(err, res) {
               if (err) return done(err);
               assert.strictEqual(res.text, 'Hello world!');
               done();
           });
    });

    // Test POST /employee
    it('POST /employee should create a new employee', function(done) {
        const newEmployee = {
            name: 'Big(O)',
            city: 'Mumbai'
        };
        employee.push(newEmployee)

        api.post('/employee')
           .send(newEmployee)
           .set('Content-Type', 'application/json')
           .expect(201)
           .end(function(err, res) {
               if (err) return done(err);
               assert.strictEqual(res.body.name, newEmployee.name);
               assert.strictEqual(res.body.city, newEmployee.city);
               assert(res.body.employeeId);
               done();
           });
    });

    // Test GET /employee/:id
    it('GET /employee/:id should return the specified employee', function(done) {
        const employeeId = employee[0].employeeId;

        api.get(`/employee/${employeeId}`)
           .expect(200)
           .end(function(err, res) {
               if (err) return done(err);
               assert.strictEqual(res.body.employeeId, employeeId);
               done();
           });
    });

    // Test GET /employee/:id with non-existing id
    it('GET /employee/:id with non-existing id should return 404', function(done) {
        const nonExistingId = 3;

        api.get(`/employee/${nonExistingId}`)
           .expect(404)
           .end(done);
    });

    // Test GET /employees/all
    it('GET /employees/all should return an array of all employees', function(done) {
        api.get('/employees/all')
           .expect(200)
           .end(function(err, res) {
               if (err) return done(err);
               assert(Array.isArray(res.body));
               done();
           });
    });

    // Test PUT /employee/:id
    it('PUT /employee/:id should update the specified employee', function(done) {
        const employeeId = employee[0].employeeId;
        const updatedEmployee = {
            name: 'NaN(O)',
            city: 'Pune'
        };

        api.put(`/employee/${employeeId}`)
           .send(updatedEmployee)
           .set('Content-Type', 'application/json')
           .expect(201)
           .end(function(err, res) {
               if (err) return done(err);
               assert.strictEqual(res.body.employeeId, employeeId);
               assert.strictEqual(res.body.name, updatedEmployee.name);
               assert.strictEqual(res.body.city, updatedEmployee.city);
               done();
           });
    });

    // Test PUT /employee/:id with non-existing id
    it('PUT /employee/:id with non-existing id should return 404', function(done) {
        const nonExistingId = 2;
        const updatedEmployee = {
            name: 'NaN(O)',
            city: 'Pune'
        };

        api.put(`/employee/${nonExistingId}`)
           .send(updatedEmployee)
           .set('Content-Type', 'application/json')
           .expect(404)
           .end(done);
    });

    // Test DELETE /employee/:id
    it('DELETE /employee/:id should delete the specified employee', function(done) {
        const employeeId = employee[0].employeeId;
        employee.shift();

        api.delete(`/employee/${employeeId}`)
           .expect(200)
           .end(function(err, res) {
               if (err) return done(err);
               assert.strictEqual(res.body.employeeId, employeeId);
               done();
           });
    });

    // Test DELETE /employee/:id with non-existing id
    it('DELETE /employee/:id with non-existing id should return 404', function(done) {
        const nonExistingId = 2;

        api.delete(`/employee/${nonExistingId}`)
           .expect(404)
           .end(done);
    });
});

function readAllEmployees() {
    try {
        const data = fs.readFileSync('../employees.json', 'utf8');
        return JSON.parse(data);
    } catch (err) {
        if (err.code === 'ENOENT') {
            console.log("File not found, creating...");
            fs.writeFileSync('../employees.json', '[]', 'utf8');
            return [];
        } else {
            console.error('Error reading file:', err);
            return [];
        }
    }
}

