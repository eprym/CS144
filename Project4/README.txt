Some corner cases we have considered:
1.When searching item with a not existed ItemID, we will show "Invalid ItemID";
2.During geocoding, if the item has latitude and longitude, we directly use it to locate.
  Else, we try to use the item's location to get its latitude and longitude and if no result is returned from google.maps, we display a global map and set the marker to (0,0).
3.When showing the result of keyword search,we show 20 items in one page. And if you press "PREV" or "NEXT", we will determine whether it is already the first page or the last page. If so,we will keep the content unchanged. 