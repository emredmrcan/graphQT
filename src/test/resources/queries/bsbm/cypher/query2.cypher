MATCH (n) WHERE n.productID = 343 RETURN n.ProductPropertyTextual_1

// MATCH (n) WHERE exists(n.ProductPropertyTextual_1) and exists(n.productID) and n.productID = 343 RETURN n.ProductPropertyTextual_1
