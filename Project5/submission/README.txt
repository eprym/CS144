Answer to questions:
	Q1:(4)→(5),(5)→(6).
	Q2:I save the information of current item in HTTP session and use the ItemID as the attribute name. When the user want to pay for an item, we retrieve the price from the session which is on the server.

The advantage of using ItemID as attribute name is that even if the user used the return button of the browser and go back to the page of another item. When the user click the "Pay Now" button on that page, we can retrieve the price of that item.

