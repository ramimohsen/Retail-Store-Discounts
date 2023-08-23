db = db.getSiblingDB('retail');

db.createCollection('users');

db.createCollection('roles');

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
    email: 'ramimohsen20@gmail.com',
    password: '$2a$10$Gpal/2RDdsZx.TpmD/rk.et36IvGdX91/ed.bXqzG/.H460uEU3gm',  // Encoded 'admin123' password
    _class: 'com.test.retailstorediscounts.entity.User'
});

db.users.insert({
    roles: [
        {$ref: 'roles', $id: affiliateRoleId}
    ], registrationDate: new Date(),
    email: 'ramimohsen30@gmail.com',
    password: '$2a$10$Gpal/2RDdsZx.TpmD/rk.et36IvGdX91/ed.bXqzG/.H460uEU3gm',  // Encoded 'admin123' password
    _class: 'com.test.retailstorediscounts.entity.User'
});

db.users.insert({
    roles: [
        {$ref: 'roles', $id: customerRoleId}
    ], registrationDate: new Date(),
    email: 'ramimohsen40@gmail.com',
    password: '$2a$10$Gpal/2RDdsZx.TpmD/rk.et36IvGdX91/ed.bXqzG/.H460uEU3gm',  // Encoded 'admin123' password
    _class: 'com.test.retailstorediscounts.entity.User'
});


db.users.createIndex({"email": 1}, {unique: true})
