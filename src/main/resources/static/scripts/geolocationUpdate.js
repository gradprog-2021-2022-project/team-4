window.onload = function (){
    if (navigator.geolocation) {
        navigator.geolocation.watchPosition(function (position) {
            document.getElementById("longitude").value = position.coords.longitude;
            document.getElementById("latitude").value = position.coords.latitude;
        });
    } else {
        document.getElementById("longitude").value = null;
        document.getElementById("latitude").value = null;
    }
    document.forms['location'].submit();
}

const { Client } = require('pg');

const client = new Client({
    host: 'ec2-34-242-8-97.eu-west-1.compute.amazonaws.com',
    user: 'fnawplbjlwccnp',
    database: 'de73793rdsuc6b',
    password: '48c1128223113cdd70e5e3e1d62c45605d52b7c1c689ee747b37ec59ac3ade17',
    port: 5432,
})

const updateUser = async (longitude, latitude,[[${}]]) =>{
    const query = `UPDATE "fash_user" SET "longitude"=$1, "latitude"=$2 WHERE "username"=$3`;

    try {
        await client.connect();          // gets connection
        await client.query(query, [longitude, latitude]); // sends queries
        return true;
    } catch (error) {
        console.error(error.stack);
        return false;
    } finally {
        await client.end();              // closes connection
    }
}