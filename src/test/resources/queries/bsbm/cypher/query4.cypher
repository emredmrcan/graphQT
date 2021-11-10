MATCH (n) WHERE n.ProductPropertyNumeric_1 = 1 and n.ProductPropertyNumeric_2 > 100 RETURN n.productID,n.ProductPropertyNumeric_1,n.ProductPropertyNumeric_2
