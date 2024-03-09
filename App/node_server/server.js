const express = require('express');
const app = express();
const fs = require('fs');
const uuid = require('uuid');
const cors = require('cors');
app.use(express.json());
app.use(cors())


// const PORT = process.env.PORT || 8080;
const PORT = 7777

let employees = readAllEmployees();

//Maintain hashmap for map eploy with their ID

let hm = new Map();

function makeHm() {
    hm.clear();
    employees.forEach(employee => {
        hm.set(employee.employeeId, employee);
    });
}

makeHm();

// Greeting 
app.get('/greeting', (req, res) => {
    return res.send('Hello world!');
});

// Register Employee
app.post('/employee', (req, res) => {
    const newEmployee = {
        employeeId: uuid.v4(),
        name: req.body.name,
        city: req.body.city
    };
    employees.push(newEmployee);
    hm.set(newEmployee.employeeId, newEmployee);
    saveAllEmployees();
    res.status(201).json({ employeeId: newEmployee.employeeId, name: newEmployee.name, city: newEmployee.city });
    return res.send();
});

// Get Employee details
app.get('/employee/:id', (req, res) => {
    const employee = hm.get(req.params.id);
    if (employee) {
        res.status(200).json(employee);
    } else {
        res.status(404).json({ message: `Employee with ${req.params.id} was not found` });
    }
    return res.send();
});

// Get all Employee details
app.get('/employees/all', (req, res) => {
    res.status(200).json(employees);
    // return res.send();
});

// Update Employee
app.put('/employee/:id', (req, res) => {
    const employee = hm.get(req.params.id);
    if (employee) {
        employee.name = req.body.name;
        employee.city = req.body.city;
        saveAllEmployees();
        res.status(201).json(employee);
    } else {
        res.status(404).json({ message: `Employee with ${req.params.id} was not found` });
    }
    return res.send();
});

// Delete All Employee
app.delete('/employee/all', (req, res) => {
    employees = []; // Clear the employees array
    hm.clear();
    saveAllEmployees();
    res.status(200).json({ message: 'All employees deleted successfully' });
});

// Delete Employee
app.delete('/employee/:id', (req, res)=>{
    const employee = hm.get(req.params.id);
    if (employee) {
        const employeeIndexToDelete = employees.findIndex(emp => emp.employeeId === req.params.id);
        employees.splice(employeeIndexToDelete, 1);
        hm.delete(req.params.id);
        saveAllEmployees();
        res.status(200).json(employee);
    } else {
        res.status(404).json({ message: `Employee with ${req.params.id} was not found` });
    }
    return res.send();
});

function readAllEmployees() {
    try {
        const data = fs.readFileSync('./employees.json', 'utf8');
        return JSON.parse(data);
    } catch (err) {
        if (err.code === 'ENOENT') {
            console.log("File not found, creating...");
            fs.writeFileSync('./employees.json', '[]', 'utf8');
            return [];
        } else {
            console.error('Error reading file:', err);
            return [];
        }
    }
}

function saveAllEmployees() {
    fs.writeFile('./employees.json', JSON.stringify(employees), err => {
        if (err) console.log("Error writing to file:", err);
    });
}

module.exports = app;

app.listen(PORT, () => {
    console.log("Server running at PORT", PORT);
});
