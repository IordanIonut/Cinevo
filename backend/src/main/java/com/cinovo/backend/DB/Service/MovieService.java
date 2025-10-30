package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Movie;
import com.cinovo.backend.DB.Repository.MovieRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.MovieStatus;
import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Response.Common.MovieResponse;
import com.cinovo.backend.TMDB.Response.DiscoverMovieResponse;
import com.cinovo.backend.TMDB.Response.MovieDetailsResponse;
import com.cinovo.backend.TMDB.Response.MovieExternalIdsResponse;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService implements TMDBLogically<Object, Object>
{
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;
    @Autowired
    private GenreService genreService;

    public List<Movie> findMovieForDiscovery() throws Exception
    {
        //TODO: add logically when find a id in database to stop to insert if not find continue to populate the database
        return (List<Movie>) this.onConvertTMDB(null);
    }

    public Movie getMovieById(final Integer id) throws Exception
    {
        Optional<Movie> movie = this.movieRepository.getMovieById(id);
        if(movie.isEmpty())
        {
            return (Movie) onConvertMovieById(id);
        }
        return movie.get();
    }

    @Override
    public Object onConvertTMDB(Object input) throws Exception
    {
        if(input == null)
        {
            return onConvertMovieList();
        }
        else if(input instanceof Integer)
        {
            return onConvertMovieById((Integer) input);
        }
        throw new IllegalArgumentException("Unsupported input type");
    }

    private List<Movie> onConvertMovieList() throws Exception
    {
        DiscoverMovieResponse discoverMovieResponse = this.service.getDiscoverMovie();
        List<Movie> movies = new ArrayList<>();
        for(MovieResponse result : discoverMovieResponse.getResults())
        {
            Movie movie = this.onConvertMovieById(result.getId());
            movies.add(movie);
        }
        this.movieRepository.saveAll(movies);
        return movies;
    }

    private Movie onConvertMovieById(final Integer id) throws Exception
    {
        MovieDetailsResponse movieResponse = this.service.getMovieDetails(id, "en-US");
        MovieExternalIdsResponse movieExternalIdsResponse = this.service.getMovieExternalIds(id);
        Movie movie = new Movie();
        movie.setAdult(movieResponse.getAdult());
        movie.setBackdrop_path(movieResponse.getBackdrop_path());
        movie.setBudget(movieResponse.getBudget());
        movie.setHomepage(movieResponse.getHomepage());
        movie.setId(movieResponse.getId());
        movie.setImdb_id(movieExternalIdsResponse.getImdb_id());
        movie.setWikidata_id(movieExternalIdsResponse.getWikidata_id());
        movie.setFacebook_id(movieExternalIdsResponse.getFacebook_id());
        movie.setInstagram_id(movieExternalIdsResponse.getInstagram_id());
        movie.setTwitter_id(movieExternalIdsResponse.getTwitter_id());
        movie.setOrigin_country(movieResponse.getOrigin_country());
        movie.setOriginal_language(movieResponse.getOriginal_language());
        movie.setOriginal_title(movieResponse.getOriginal_title());
        movie.setOverview(movieResponse.getOverview());
        movie.setPopularity(movieResponse.getPopularity());
        movie.setPoster_path(movieResponse.getPoster_path());
        movie.setRelease_date(LocalDate.parse(movieResponse.getRelease_date()));
        movie.setRevenue(movieResponse.getRevenue());
        movie.setRuntime(movieResponse.getRuntime());
        movie.setStatus(MovieStatus.valueOf(movieResponse.getStatus().toUpperCase()));
        movie.setTagline(movieResponse.getTagline());
        movie.setTitle(movieResponse.getTitle());
        movie.setVideo(movieResponse.getVideo());
        movie.setVote_average(movieResponse.getVote_average());
        movie.setVote_count(movieResponse.getVote_count());
        movie.setGenres(genreService.parsGenreToObjects(movieResponse.getGenres(), Type.MOVIE));

        if(movieResponse.getBelongs_to_collection() != null)
        {
            Movie.BelongToCollection belongToCollection = new Movie.BelongToCollection();
            belongToCollection.setId(movieResponse.getBelongs_to_collection().getId());
            belongToCollection.setName(movieResponse.getBelongs_to_collection().getName());
            belongToCollection.setPoster_path(movieResponse.getBelongs_to_collection().getPoster_path());
            belongToCollection.setBackdrop_path(movieResponse.getBelongs_to_collection().getBackdrop_path());

            movie.setBelong_to_collection(belongToCollection);
        }

        if(movieResponse.getProduction_companies() != null)
        {
            List<Movie.ProductionCompany> productionCompanies = new ArrayList<>();
            for(MovieDetailsResponse.ProductionCompany company : movieResponse.getProduction_companies())
            {
                Movie.ProductionCompany productionCompany = new Movie.ProductionCompany();
                productionCompany.setId(company.getId());
                productionCompany.setName(company.getName());
                productionCompany.setLogo_path(company.getLogo_path());
                productionCompany.setOrigin_country(company.getOrigin_country());
                productionCompanies.add(productionCompany);
            }
            movie.setProduction_companies(productionCompanies);
        }

        if(movieResponse.getProduction_countries() != null)
        {
            List<Movie.ProductionCountry> productionCountries = new ArrayList<>();
            for(MovieDetailsResponse.ProductionCountry country : movieResponse.getProduction_countries())
            {
                Movie.ProductionCountry productionCountry = new Movie.ProductionCountry();
                productionCountry.setIso_3166_1(country.getIso_3166_1());
                productionCountry.setName(country.getName());
                productionCountries.add(productionCountry);
            }
            movie.setProduction_countries(productionCountries);
        }

        if(movieResponse.getSpoken_languages() != null)
        {
            List<Movie.SpokenLanguage> spokenLanguages = new ArrayList<>();
            for(MovieDetailsResponse.SpokenLanguage language : movieResponse.getSpoken_languages())
            {
                Movie.SpokenLanguage spokenLanguage = new Movie.SpokenLanguage();
                spokenLanguage.setEnglish_name(language.getEnglish_name());
                spokenLanguage.setIso_639_1(language.getIso_639_1());
                spokenLanguage.setName(language.getName());
                spokenLanguages.add(spokenLanguage);
            }
            movie.setSpoken_languages(spokenLanguages);
        }

        movieRepository.save(movie);
        return movie;
    }
}
