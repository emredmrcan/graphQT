MATCH (n) WHERE exists(n.productName) and exists(n.unitsInStock) RETURN n.productName,n.unitsInStock