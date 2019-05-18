SEARCH-APP


This app search for trees in a given area

This is an app developed on spring-boot with Jersey implementation of jax-rs. This app seraches for list of tress in given area and radius and give the count of trees. The input to the app comes from https://data.cityofnewyork.us/resource/nwxe-4ae8.json

We can use the URL http://localhost:8080/search?x=1&y=22&radius=123 to search for the trees in the given area x and y denotes the point and radius defines the raidius around which the list of trees need to be found

If there are any trees present with in the given point and radius provided in the input the service returns the trees along with the count of the trees present in that area. If there are no trees present in the given Area, it returs No Trees present in the given area.
