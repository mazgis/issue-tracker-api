create table issue (
	issue_id bigint auto_increment primary key,
	issue_type text not null,
	title text not null,
	description text,
	status text not null,
	priority text,
	story_points int,
	created_at timestamp(3) with time zone,
	developer_id bigint,
	foreign key (developer_id) references developer(id)
);