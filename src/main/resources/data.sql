show databases;

--- 일단 3개만 테스트용 데이터를 추가하자.

insert into article (title, content, hashtag, created_at, created_by, modified_at, modified_by) values
('첫번째 게시글', '내용#1', '#human', '2024-01-01 12:00', '홍길동', '2024-01-01 12:00', '홍길동'),
('두번째 게시글', '내용#2', '#dog', '2024-01-02 13:00', '흰둥이', '2024-01-02 13:00', '흰둥이'),
('세번째 게시글', '내용#3', '#dog', '2024-01-03 14:00', '멍멍이', '2024-01-03 14:00', '멍멍이');


insert into article_comment (article_id, content, created_at, created_by, modified_at, modified_by) values
(1, '댓글1-1', '2024-01-01 12:30', '냐옹이', '2024-01-01 12:30', '냐옹이'),
(3, '댓글3-1', '2024-01-02 13:30', '삐약이', '2024-01-02 13:30', '삐약이'),
(3, '댓글3-2', '2024-01-03 14:30', '꿀꿀이', '2024-01-03 14:30', '꿀꿀이');

