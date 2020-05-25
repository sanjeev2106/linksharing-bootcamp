# -*- coding: utf-8 -*-

from bs4 import BeautifulSoup
import csv
import requests 
from Tkinter import *
import Tkinter as ttk
from ttk import *


root = Tk()
root.title("Webscrapper")
 
# Add a grid
mainframe = Frame(root)
mainframe.grid(column=0,row=0, sticky=(N,W,E,S) )
mainframe.columnconfigure(0, weight = 1)
mainframe.rowconfigure(0, weight = 1)
mainframe.pack(pady = 50, padx = 50)
 
# Create a Tkinter variable
list1 = StringVar(root)
list2 = StringVar(root)
 
# Dictionary with options
PRODUCTS = { 'Mobile','Laptops'}
list1.set('Mobile') # set the default option

MOBILES={'MI','Apple','Samsung'}
list2.set('MI') # set the default option
 
popupMenu = OptionMenu(mainframe,list1, *PRODUCTS)
Label(mainframe, text="Products").grid(row = 1, column = 1)
popupMenu.grid(row = 2, column =1)

popupMenu = OptionMenu(mainframe, list2, *MOBILES)
Label(mainframe, text="Mobiles").grid(row = 1, column = 3)
popupMenu.grid(row = 2, column =3)

button = ttk.Button(root, text="OK", command=lambda: mainMethod(list1.get(),list2.get()))
button.pack()

def mainMethod(product, mobile):

	print(product)
	print(mobile)
	applePhones_url = 'https://www.flipkart.com/search?q=iphone&sid=tyy%2C4io&as=on&as-show=on&otracker=AS_QueryStore_HistoryAutoSuggest_0_6&otracker1=AS_QueryStore_HistoryAutoSuggest_0_6&as-pos=0&as-type=HISTORY&as-searchtext=iphone'

	miPhones_url = 'https://www.flipkart.com/search?q=mi+mobiles&sid=tyy%2C4io&as=on&as-show=on&otracker=AS_QueryStore_OrganicAutoSuggest_0_10&otracker1=AS_QueryStore_OrganicAutoSuggest_0_10&as-pos=0&as-type=RECENT&as-searchtext=mi%20mobiles'

#-----------------------------------------------
	#fetch records for MI phones
	if(product == 'Mobile' and mobile == 'MI'):
		print("Fetching records for MI phones")
		fetchRecords("MI_PHONES.csv", miPhones_url)
		print("Records saved in MI_PHONES.csv")




#-----------------------------------------------
	#fetch records for MI phones
	elif(product == 'Mobile' and mobile == 'Apple'):
		print("Fetching records for APPLE phones")
		fetchRecords("APPLE_PHONES.csv", applePhones_url)
		print("Records saved in APPLE_PHONES.csv")

#-----------------------------------------------------------------
	#for others
	else:
		print("Soryy Work in progress");


#---------------------------------------------------------------------------------------------------------

def fetchRecords(productFileName, url):
	

	request = requests.get(url)
	page = BeautifulSoup(request.text, 'html.parser')
	URLs = page.findAll("a", {"class":"_2Xp0TH"})

	filename = productFileName
	f = open(filename,"w")
	f.write("")
	f.close()
	f = open(filename,"a")
	headers="Product_Name\t\t\tPricing\t\t\tRating\n"
	f.write(headers)

	for url in URLs:
		print("https://www.flipkart.com"+url.get('href'))
		r = requests.get("https://www.flipkart.com"+url.get('href'))
		page_soup = BeautifulSoup(r.text, 'html.parser')

		containers = page_soup.findAll("div", {"class": "_3O0U0u"})

		

		try:
			container = containers[0]		
			price = container.findAll("div",{"class":"_1vC4OE _2rQ-NK"})		
			rating = container.findAll("div",{"class":"hGSR34"})
			


			for container in containers:
			    product_name = container.div.img["alt"]

			    price_container = container.findAll("div", {"class": "_1vC4OE _2rQ-NK"})

			    price = price_container[0].text.strip()



			    rating_container = container.findAll("div", {"class": "hGSR34"})
			    rating = rating_container[0].text

			    f.write(product_name+"\t\t")
			    f.write(price.encode('utf-8')+"\t\t")
			    f.write(rating)
			    f.write("\n")
		except:
			print("")

	f.close()


root.mainloop()



