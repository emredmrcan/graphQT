MATCH (a)-[]->(product) WHERE a.type = "review" RETURN count(distinct product)