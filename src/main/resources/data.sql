INSERT INTO role (role_name)
VALUES ('ADMIN');
INSERT INTO role (role_name)
VALUES ('TRENER');
INSERT INTO role (role_name)
VALUES ('ANALITYK');

INSERT INTO users (uuid, login, password, role_id, email, islock, isenabled)
VALUES ('uuid', 'admin', '$2a$12$.cQNsTEWaS1hN3GPEqM57OgZ2Qe55vgzV4BUKTehSb3DRywAD5PBW', 1, 'admin@example.com', 0, 1);

INSERT INTO league (league_id, name, country, logo)
VALUES (1, 'Test liga', 'Test', 'test');

INSERT INTO team (team_id, name, logo)
VALUES (1, 'Drużyna A', 'test');
INSERT INTO team (team_id, name, logo)
VALUES (2, 'Drużyna B', 'test');

INSERT INTO league_team (team_id, league_id)
VALUES (1, 1);
INSERT INTO league_team (team_id, league_id)
VALUES (2, 1);

INSERT INTO users (uuid, login, password, role_id, email, islock, isenabled, team_id)
VALUES ('ac87cba6-97e0-43bb-adac-f4869e78d797', 'test', '$2a$10$F9.Wr5YYQc0GlJt8BxO0QO8Lc6G54pqtUGer3U7vNWDynoBaCNCq2',
        2, 'test@example.com', 0, 1, 1);

INSERT INTO player (player_id, name, team_id, photo)
VALUES (1, 'Zawodnik A1', 1, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (2, 'Zawodnik A2', 1, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (3, 'Zawodnik A3', 1, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (4, 'Zawodnik A4', 1, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (5, 'Zawodnik A5', 1, 'test');

INSERT INTO player (player_id, name, team_id, photo)
VALUES (6, 'Zawodnik B1', 2, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (7, 'Zawodnik B2', 2, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (8, 'Zawodnik B3', 2, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (9, 'Zawodnik B4', 2, 'test');
INSERT INTO player (player_id, name, team_id, photo)
VALUES (10, 'Zawodnik B5', 2, 'test');

INSERT INTO fixture (fixture_id, league_id, home_team_id, away_team_id, home_goals, away_goals, date, is_counted,
                     is_collected, season)
VALUES (1, 1, 1, 2, 4, 0, '2024-05-01 12:00:00', 1, 0, 2024);

INSERT INTO fixture (fixture_id, league_id, home_team_id, away_team_id, home_goals, away_goals, date, is_counted,
                     is_collected, season)
VALUES (2, 1, 1, 2, 3, 1, '2024-05-08 12:00:00', 1, 0, 2024);

INSERT INTO fixture (fixture_id, league_id, home_team_id, away_team_id, home_goals, away_goals, date, is_counted,
                     is_collected, season)
VALUES (3, 1, 1, 2, 2, 2, '2024-05-15 12:00:00', 1, 0, 2024);

INSERT INTO fixture (fixture_id, league_id, home_team_id, away_team_id, home_goals, away_goals, date, is_counted,
                     is_collected, season)
VALUES (4, 1, 1, 2, 1, 3, '2024-05-22 12:00:00', 1, 0, 2024);

INSERT INTO fixture (fixture_id, league_id, home_team_id, away_team_id, home_goals, away_goals, date, is_counted,
                     is_collected, season)
VALUES (5, 1, 1, 2, 0, 4, '2024-05-29 12:00:00', 1, 0, 2024);

INSERT INTO fixture (fixture_id, league_id, home_team_id, away_team_id, home_goals, away_goals, date, is_counted,
                     is_collected, season)
VALUES (6, 1, 1, 2, -1, -1, '2025-05-29 12:00:00', 0, 0, 2025);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 1, 1, 90, 'M', 5.5, 5, 4, 2, 1, 0, 1, 1, 50, 3, 40, 4, 1, 2, 7, 4, 10, 6, 3, 3, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 2, 1, 90, 'M', 8.0, 0, 4, 2, 1, 0, 2, 0, 45, 3, 40, 3, 1, 2, 7, 5, 4, 3, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 3, 1, 90, 'M', 7.5, 0, 3, 1, 1, 0, 1, 0, 50, 2, 40, 4, 1, 1, 6, 4, 3, 2, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 4, 1, 90, 'D', 7.0, 0, 2, 1, 0, 0, 0, 0, 30, 1, 22, 6, 2, 3, 5, 3, 2, 1, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 5, 1, 90, 'G', 7.5, 0, 6, 3, 1, 0, 0, 4, 20, 0, 16, 1, 0, 2, 3, 2, 1, 1, 1, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 6, 2, 90, 'F', 5.5, 3, 2, 1, 0, 4, 0, 2, 25, 2, 20, 2, 0, 1, 5, 3, 2, 1, 2, 2, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 7, 2, 90, 'M', 5.0, 2, 2, 0, 0, 4, 0, 0, 20, 1, 11, 1, 0, 0, 4, 2, 1, 0, 1, 2, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 8, 2, 90, 'M', 5.0, 1, 1, 0, 0, 4, 0, 0, 15, 0, 11, 2, 1, 1, 3, 2, 1, 1, 0, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 9, 2, 90, 'D', 5.5, 0, 0, 0, 0, 4, 0, 0, 20, 0, 18, 4, 2, 2, 4, 3, 0, 0, 1, 1, 0, 1);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (1, 10, 2, 90, 'G', 6.0, 0, 0, 0, 0, 4, 0, 6, 10, 0, 9, 1, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 1, 1, 90, 'M', 8.0, 1, 5, 3, 2, 1, 1, 0, 45, 3, 41, 3, 1, 2, 8, 6, 5, 4, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 2, 1, 90, 'M', 7.5, 0, 4, 2, 1, 1, 1, 0, 50, 4, 44, 4, 1, 2, 7, 5, 4, 3, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 3, 1, 90, 'M', 7.0, 0, 3, 1, 0, 1, 0, 0, 40, 2, 32, 3, 1, 2, 6, 4, 3, 2, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 4, 1, 90, 'D', 7.0, 0, 2, 1, 0, 1, 0, 0, 35, 1, 33, 5, 2, 3, 5, 3, 2, 1, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 5, 1, 90, 'G', 7.5, 0, 0, 0, 0, 1, 0, 3, 20, 0, 16, 1, 0, 2, 4, 3, 1, 1, 0, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 6, 2, 90, 'F', 6.0, 1, 3, 1, 1, 3, 0, 2, 30, 2, 28, 2, 1, 1, 6, 4, 3, 2, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 7, 2, 90, 'M', 5.5, 1, 2, 1, 0, 3, 0, 1, 25, 1, 22, 2, 0, 1, 5, 3, 2, 1, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 8, 2, 90, 'M', 5.5, 0, 1, 0, 0, 3, 0, 0, 20, 0, 18, 3, 1, 2, 4, 3, 2, 1, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 9, 2, 90, 'D', 5.5, 0, 0, 0, 0, 3, 0, 0, 15, 0, 12, 4, 1, 2, 4, 3, 1, 0, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (2, 10, 2, 90, 'G', 6.0, 0, 0, 0, 0, 3, 0, 4, 15, 0, 12, 2, 1, 0, 3, 2, 0, 0, 0, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 1, 1, 90, 'M', 7.5, 1, 5, 3, 1, 2, 1, 0, 40, 3, 33, 3, 1, 2, 7, 5, 4, 3, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 2, 1, 90, 'M', 7.5, 0, 4, 2, 1, 2, 0, 0, 45, 5, 31, 4, 1, 4, 8, 6, 5, 3, 2, 2, 0, 1);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 3, 1, 90, 'M', 7.0, 0, 3, 2, 0, 2, 0, 0, 50, 3, 45, 3, 2, 2, 6, 4, 3, 2, 3, 4, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 4, 1, 90, 'D', 7.0, 0, 1, 1, 0, 2, 1, 0, 30, 1, 31, 5, 2, 6, 6, 4, 2, 2, 1, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 5, 1, 90, 'G', 7.0, 0, 0, 0, 0, 2, 0, 4, 25, 0, 21, 1, 3, 2, 4, 2, 1, 1, 2, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 6, 2, 90, 'F', 7.5, 1, 5, 3, 1, 2, 1, 0, 40, 3, 33, 3, 1, 2, 7, 5, 4, 3, 2, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 7, 2, 90, 'M', 7.5, 0, 4, 2, 1, 2, 0, 0, 45, 4, 36, 4, 3, 3, 8, 6, 5, 3, 1, 3, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 8, 2, 90, 'M', 7.0, 0, 3, 2, 0, 2, 2, 0, 50, 2, 41, 3, 1, 1, 6, 4, 3, 2, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 9, 2, 90, 'D', 7.0, 0, 1, 1, 0, 2, 0, 0, 30, 5, 24, 5, 2, 3, 6, 4, 2, 2, 4, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (3, 10, 2, 90, 'G', 7.0, 0, 0, 0, 0, 2, 0, 4, 25, 0, 22, 1, 1, 3, 4, 2, 1, 1, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 1, 1, 90, 'M', 6.5, 1, 4, 2, 1, 3, 0, 0, 40, 2, 29, 3, 1, 2, 6, 4, 3, 2, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 2, 1, 90, 'M', 6.0, 0, 3, 1, 0, 3, 0, 0, 35, 1, 31, 2, 0, 1, 5, 3, 2, 1, 1, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 3, 1, 90, 'M', 6.0, 0, 2, 1, 0, 3, 0, 0, 30, 1, 24, 3, 1, 2, 5, 3, 2, 2, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 4, 1, 90, 'D', 6.0, 0, 1, 1, 0, 3, 1, 0, 25, 1, 21, 5, 2, 3, 4, 3, 1, 1, 1, 2, 0, 1);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 5, 1, 90, 'G', 6.0, 0, 0, 0, 0, 3, 0, 5, 20, 0, 17, 1, 0, 2, 4, 3, 1, 0, 0, 3, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 6, 2, 90, 'F', 8.0, 2, 6, 4, 2, 1, 0, 0, 50, 4, 44, 3, 1, 2, 8, 6, 5, 4, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 7, 2, 90, 'M', 7.5, 1, 5, 5, 1, 1, 1, 0, 45, 3, 41, 4, 1, 3, 7, 5, 4, 3, 2, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 8, 2, 90, 'M', 7.5, 0, 4, 2, 0, 1, 0, 0, 40, 2, 33, 3, 1, 2, 6, 4, 3, 2, 1, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 9, 2, 90, 'D', 7.0, 0, 2, 1, 0, 1, 1, 0, 30, 1, 28, 5, 2, 3, 5, 3, 2, 1, 1, 4, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (4, 10, 2, 90, 'G', 7.5, 0, 0, 0, 0, 1, 0, 6, 20, 0, 17, 1, 0, 2, 4, 3, 1, 1, 0, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 1, 1, 90, 'M', 5.0, 1, 2, 1, 0, 4, 0, 0, 25, 1, 22, 3, 1, 1, 5, 3, 2, 1, 1, 1, 0, 1);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 2, 1, 90, 'M', 5.0, 0, 2, 0, 0, 4, 0, 0, 30, 1, 25, 3, 1, 2, 4, 2, 2, 1, 1, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 3, 1, 90, 'M', 5.0, 0, 1, 0, 0, 4, 0, 0, 20, 1, 17, 3, 1, 1, 4, 2, 2, 1, 1, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 4, 1, 90, 'D', 5.0, 0, 1, 0, 0, 4, 0, 0, 25, 0, 22, 4, 2, 3, 5, 3, 1, 1, 1, 1, 0, 0);
INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 5, 1, 90, 'G', 5.5, 0, 0, 0, 0, 4, 0, 5, 15, 0, 12, 1, 0, 1, 3, 2, 0, 0, 1, 0, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 6, 2, 90, 'F', 9.0, 2, 7, 5, 0, 0, 1, 0, 50, 4, 43, 3, 2, 3, 10, 8, 6, 5, 3, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 7, 2, 90, 'M', 8.5, 1, 6, 4, 2, 0, 2, 0, 45, 3, 41, 4, 1, 2, 9, 7, 5, 4, 2, 1, 1, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 8, 2, 90, 'M', 8.0, 1, 5, 3, 1, 0, 1, 0, 40, 2, 36, 3, 1, 2, 7, 5, 4, 3, 1, 1, 0, 0);

INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 9, 2, 90, 'D', 7.5, 0, 3, 2, 1, 0, 0, 0, 35, 1, 33, 5, 2, 3, 6, 4, 2, 2, 1, 0, 0, 0);
INSERT INTO fixtures_stats (fixture_id, player_id, team_id, minutes, position, rating, offsides,
                            shots_total, shots_on_goal, goals_total, goals_conceded, assists, saves,
                            passes_total, passes_key, passes_accuracy, tackles_total, tackles_blocks,
                            tackles_interceptions, duels_total, duels_won, dribbles_attempts,
                            dribbles_success, fouls_drawn, fouls_committed, cards_yellow, cards_red)
VALUES (5, 10, 2, 90, 'G', 8.0, 0, 0, 0, 0, 0, 0, 6, 25, 0, 22, 2, 1, 2, 5, 4, 2, 1, 0, 0, 0, 0);
