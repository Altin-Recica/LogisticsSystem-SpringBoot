INSERT INTO material (material_id, name, description)
VALUES (gen_random_uuid(), 'Gips',
        'Gips is een zacht sulfaatmineraal bestaande uit calciumsulfaatdihydraat. Het wordt vaak gebruikt in de bouwsector voor de productie van gips, gipsplaat en cement. Gips wordt in de landbouw ook gebruikt als bodemverbeteraar en meststof.'),

       (gen_random_uuid(), 'Ijzererts',
        'IJzererts is een natuurlijk voorkomend mineraal waaruit ijzer wordt gewonnen. Het is een cruciale grondstof bij de productie van staal, dat op grote schaal wordt gebruikt in de bouw-, productie- en transportindustrie. Veel voorkomende soorten ijzererts zijn hematiet en magnetiet.'),

       (gen_random_uuid(), 'Cement',
        'Cement is een bindmiddel dat in de bouw wordt gebruikt en dat uithardt en aan andere materialen hecht om ze samen te binden. Het is een belangrijk ingrediënt in beton, mortel en stucwerk. Portlandcement, gemaakt van kalksteen en klei, is het meest voorkomende type.'),

       (gen_random_uuid(), 'Petcoke',
        'Petcoke is een koolstofrijk vast materiaal afkomstig van olieraffinage. Vanwege de hoge calorische waarde wordt het gebruikt als brandstof bij energieopwekking, cementovens en andere industriële processen. Petcoke wordt ook gebruikt bij de productie van elektroden voor de aluminium- en staalindustrie.'),

       (gen_random_uuid(), 'Slak',
        'Slak is een bijproduct van het smeltproces dat wordt gebruikt om metalen uit hun ertsen te produceren. Het wordt in de bouw gebruikt als aggregaat in beton, in de wegenbouw en als grondstof bij de cementproductie. Slak helpt de duurzaamheid en sterkte van beton te verbeteren.');

INSERT INTO warehouse (warehouseid, warehouse_number, material_type, current_capacity)
VALUES (gen_random_uuid(), 1, 'Gips', 3500.0),
       (gen_random_uuid(), 2, 'Ijzererts', 7500.0),
       (gen_random_uuid(), 3, 'Cement', 7500.0),
       (gen_random_uuid(), 4, 'Petcoke', 7500.0),
       (gen_random_uuid(), 5, 'Slak', 7500.0);

INSERT INTO material_inventory (material_inventory_id, client_number, quantity, arrival_date,
                                material_id, warehouse_id)
VALUES (gen_random_uuid(), 1, 500.0, '2024-09-01', (SELECT material_id FROM material WHERE name = 'Gips'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 1)),
       (gen_random_uuid(), 1, 500.0, '2024-09-01', (SELECT material_id FROM material WHERE name = 'Gips'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 1)),
       (gen_random_uuid(), 1, 200.0, '2024-09-02', (SELECT material_id FROM material WHERE name = 'Ijzererts'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 2)),
       (gen_random_uuid(), 1, 300.0, '2024-09-05', (SELECT material_id FROM material WHERE name = 'Cement'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 3)),
       (gen_random_uuid(), 2, 400.0, '2024-09-07', (SELECT material_id FROM material WHERE name = 'Petcoke'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 4)),
       (gen_random_uuid(), 2, 300.0, '2024-09-05', (SELECT material_id FROM material WHERE name = 'Slak'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 5)),
       (gen_random_uuid(), 2, 500.0, '2024-09-07', (SELECT material_id FROM material WHERE name = 'Slak'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 5)),
       (gen_random_uuid(), 2, 500.0, '2024-09-01', (SELECT material_id FROM material WHERE name = 'Gips'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 1)),
       (gen_random_uuid(), 3, 500.0, '2024-09-07', (SELECT material_id FROM material WHERE name = 'Slak'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 5)),
       (gen_random_uuid(), 3, 500.0, '2024-09-01', (SELECT material_id FROM material WHERE name = 'Gips'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 1)),
       (gen_random_uuid(), 3, 200.0, '2024-09-02', (SELECT material_id FROM material WHERE name = 'Ijzererts'),
        (SELECT warehouseid FROM warehouse WHERE warehouse_number = 2));

INSERT INTO client (clientid, name, email)
VALUES (1, 'Krystal Mineral Supplies', 'info@krystalmineralsupplies.com'),
       (2, 'Eco Earth Resources', 'contact@ecoearthresources.com'),
       (3, 'Atlas Industrial Metals', 'sales@atlasindustrial.com');