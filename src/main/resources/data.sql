show databases;

--- 일단 3개만 테스트용 데이터를 추가하자.

insert into article (user_account_id, title, content, hashtag, created_at, created_by, modified_at, modified_by) values
(1, '첫번째 게시글', '내용#1', '#human', '2024-01-01 12:00', '홍길동', '2024-01-01 12:00', '홍길동'),
(2, '두번째 게시글', '내용#2', '#dog', '2024-01-02 13:00', '흰둥이', '2024-01-02 13:00', '흰둥이'),
(3, '세번째 게시글', '내용#3', '#dog', '2024-01-03 14:00', '멍멍이', '2024-01-03 14:00', '멍멍이');


insert into article_comment (article_id, user_account_id, content, created_at, created_by, modified_at, modified_by) values
(1, 3, '댓글1-1', '2024-01-01 12:30', '멍멍이', '2024-01-01 12:30', '멍멍이'),
(3, 1, '댓글3-1', '2024-01-02 13:30', '홍길동', '2024-01-02 13:30', '홍길동'),
(3, 3, '댓글3-2', '2024-01-03 14:30', '멍멍이', '2024-01-03 14:30', '멍멍이');


-- 계정 정보
-- TODO: 테스트용이지만 비밀번호가 노출된 데이터 세팅. 개선하는 것이 좋을지 고민이다.
insert into user_account (user_id, user_password, nickname, email, memo, created_at, created_by, modified_at, modified_by) values
('hgd', '12345', '길동이', 'abc@gmail.com', 'I am 홍길동.', now(), '홍길동', now(), '홍길동');
