-- Place your queries here. Docs available https://www.hugsql.org/
-- :name get-lists :query :one
select * from list where user_id = :user_id

-- :name update-lists :execute
insert into list (user_id, detail)
values (:user_id, :detail)
on conflict(user_id) do update set detail = :detail
