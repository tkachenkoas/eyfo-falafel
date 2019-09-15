CREATE OR REPLACE FUNCTION update_place_avg_rating()
    RETURNS trigger AS '
    BEGIN
        UPDATE t_places pl
        SET avg_rating = (
            SELECT AVG(rating)
            FROM t_reviews rv
            WHERE rv.place_id = NEW.place_id
            GROUP BY rv.place_id
        )
        WHERE pl.id = NEW.place_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER calculateAverageRating
AFTER INSERT ON t_reviews
FOR EACH ROW EXECUTE PROCEDURE update_place_avg_rating();