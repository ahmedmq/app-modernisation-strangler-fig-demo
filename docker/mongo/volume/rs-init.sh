#!/bin/bash

MONGODB1=mongodb

echo "**********************************************" ${MONGODB1}
echo "Waiting for startup.."
sleep 10
echo "done"

echo SETUP.sh time now: "$(date +"%T" )"
mongosh --host ${MONGODB1}:27017 <<EOF
var cfg = {
    "_id": "dbrs",
    "protocolVersion": 1,
    "version": 1,
    "members": [
        {
            "_id": 0,
            "host": "${MONGODB1}:27017"
        }
    ]
};
rs.initiate(cfg, { force: true });
rs.status();
EOF