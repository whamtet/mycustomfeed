-- Place your queries here. Docs available https://www.hugsql.org/
-- :name get-list :query :one
select * from list where user_id = :user_id
