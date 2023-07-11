# Omniward Client - Simple Patients Directory App

This is a simple frontend web application for managing a patients directory. It allows you to browse, search, create, delete, and edit patient records.

## Technologies Used

ClojureScript with [Reagent](http://reagent-project.github.io), [Re-frame](https://github.com/day8/re-frame) and [Shadow-cljs](https://github.com/thheller/shadow-cljs).
[Vercel](http://vercel.com/) for hosting the live version of the app.


## Local Development

To run the app locally on your machine, follow these instructions:

Pre:
Ensure The Local Server is up and running:

1. Clone the GitHub repository:

```bash
git clone https://github.com/kruzabasi/omniward-client.git 
```
2. Install the required dependencies and start the app:

```bash
cd omniward-client
npm install
npm run start

npx shadow-cljs cljs-repl app
```
## Features

* Browse all patients with their details displayed in cards.
* Search for patients by their names.
* Create new patient records with a simple form.
* Delete patients from the directory with a confirmation modal.
* Edit patient details using an editable form displayed in a modal.

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests for any improvements or bug fixes.
