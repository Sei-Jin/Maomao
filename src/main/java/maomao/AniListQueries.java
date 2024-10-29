package maomao;

public class AniListQueries
{
    private static final String NEW_ACTIVITIES_QUERY = sanitizeQuery("""
            query ($userId: Int, $lastActivityTime: Int) {
                Page {
                    activities (userId: $userId, createdAt_greater: $lastActivityTime) {
                        __typename
                        ... on ListActivity {
                            createdAt
                            status
                            media {
                                id
                                type
                                title {
                                    english
                                }
                            }
                            user {
                                name
                                avatar {
                                    medium
                                }
                            }
                        }
                    }
                }
            }
            """);
    
    private static final String LATEST_ACTIVITY_TIME_QUERY = sanitizeQuery("""
                query ($userId: Int) {
                    Activity (userId: $userId, sort: ID_DESC) {
                            __typename
                            ... on ListActivity {
                                createdAt
                        }
                    }
                }
                """);
    
    private static final String USER_DATA_QUERY = """
                query ($username: String) {
                    User (search: $username) {
                        id
                        updatedAt
                    }
                }
                """;
    
    public String getNewActivities()
    {
        return NEW_ACTIVITIES_QUERY;
    }
    
    public String getLatestActivityTime()
    {
        return LATEST_ACTIVITY_TIME_QUERY;
    }
    
    public String getUserData()
    {
        return USER_DATA_QUERY;
    }
    
    private static String sanitizeQuery(String query)
    {
        return query.replace('\n', ' ');
    }
}
