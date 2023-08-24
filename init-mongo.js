db = db.getSiblingDB('retail');

db.createCollection('users');

db.createCollection('roles');

db.createCollection('discount_rules');

const employeeRoleId = ObjectId();
const affiliateRoleId = ObjectId();
const customerRoleId = ObjectId();

db.roles.insertOne({_id: employeeRoleId, name: 'ROLE_EMPLOYEE'});
db.roles.insertOne({_id: affiliateRoleId, name: 'ROLE_AFFILIATE'});
db.roles.insertOne({_id: customerRoleId, name: 'ROLE_CUSTOMER'});

db.users.insert({
    roles: [
        {$ref: 'roles', $id: employeeRoleId},
        {$ref: 'roles', $id: affiliateRoleId}
    ], registrationDate: new Date(),
    email: 'test1@gmail.com',
    password: '$2a$10$Gpal/2RDdsZx.TpmD/rk.et36IvGdX91/ed.bXqzG/.H460uEU3gm',  // Encoded 'admin123' password
    _class: 'com.test.retailstorediscounts.entity.User'
});

db.users.insert({
    roles: [
        {$ref: 'roles', $id: affiliateRoleId}
    ], registrationDate: new Date(),
    email: 'test2@gmail.com',
    password: '$2a$10$Gpal/2RDdsZx.TpmD/rk.et36IvGdX91/ed.bXqzG/.H460uEU3gm',  // Encoded 'admin123' password
    _class: 'com.test.retailstorediscounts.entity.User'
});

db.users.insert({
    roles: [
        {$ref: 'roles', $id: customerRoleId}
    ], registrationDate: new Date(),
    email: 'test3@gmail.com',
    password: '$2a$10$Gpal/2RDdsZx.TpmD/rk.et36IvGdX91/ed.bXqzG/.H460uEU3gm',  // Encoded 'admin123' password
    _class: 'com.test.retailstorediscounts.entity.User'
});

db.discount_rules.insert({
    name: 'EMPLOYEE',
    description: 'Employee discount rule',
    discount_parentage: 30,
    active: true
});

db.discount_rules.insert({
    name: 'AFFILIATE',
    description: 'Affiliate discount rule',
    discount_parentage: 10,
    active: true
});

db.discount_rules.insert({
    name: 'CUSTOMER',
    description: 'Customer for over 2 years discount rule',
    discount_parentage: 5,
    active: true
});

db.discount_rules.insert({
    name: 'Bill100',
    description: 'For every $100 on the bill, there would be a $ 5 discount (e.g. for $ 990, you get $ 45 as\n' +
        'a discount)',
    discount_parentage: 5,
    active: true
});

db.users.createIndex({"email": 1}, {unique: true})

db.discount_rules.createIndex({"name": 1}, {unique: true})
