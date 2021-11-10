MATCH (a {reviewerID:86})-[]->(r) WHERE exists(r.Rating_2) RETURN a.identity,r.identity,r.Rating_2
