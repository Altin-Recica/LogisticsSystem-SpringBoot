INSERT INTO Truck (truckid, capacity, licence_plate, current_weight, tare_weight, arrival_time, has_appointment, weigh_number)
VALUES (gen_random_uuid(), 12000.00, '1-OPS-847', 8000.00, 4000.00, (CURRENT_DATE + INTERVAL '19:30:00' HOUR TO MINUTE), FALSE, 0),
       (gen_random_uuid(), 18000.00, '2-ETS-102', 12000.00, 8000.00, (CURRENT_DATE + INTERVAL '08:30:00' HOUR TO MINUTE), FALSE, 0),
       (gen_random_uuid(), 8000.00, '1-AZH-275', 4000.00, 2000.00, (CURRENT_DATE + INTERVAL '09:30:00' HOUR TO MINUTE), FALSE, 0),
       (gen_random_uuid(), 16000.00, '3-RYG-745', 9500.00, 5000.00, (CURRENT_DATE + INTERVAL '11:30:00' HOUR TO MINUTE), FALSE, 0),
       (gen_random_uuid(), 14000.00, '1-JFT-367', 7000.00, 3500.00, (CURRENT_DATE + INTERVAL '10:30:00' HOUR TO MINUTE), FALSE, 0);

INSERT INTO weighbridge (weighbridgeid, available)
VALUES (1, TRUE),
       (2, TRUE);

INSERT INTO fifoqueue (queueid)
VALUES (gen_random_uuid());

INSERT INTO terrain (terrainid)
VALUES (gen_random_uuid());
