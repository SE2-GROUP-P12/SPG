# File naming

all the mysql db script are in data folder.

the name of the script should be
`xxx_nn_name.sql`
**where:** 
 + `xxx` is the version of the release
 + `nn` is the order of the scripts
 
examples:
script `001_01_users.sql`, `001_02_wallet.sql`, `001_03_product.sql`, `010_01_user.sql` will be execute in this order:
 + 001_01
 + 001_02
 + 001_03
 + 010_01