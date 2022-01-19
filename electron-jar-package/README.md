> A jhipster module to run spring boot app (**target/jar**) in electron package generated with [generator-jhipster-electron](https://github.com/amanganiello90/generator-jhipster-electron)

# Installation

Run

```bash
yarn
```

or

```bash
npm install
```

# usage

After installing dependencies and generating **target/\*.jar** with spring-boot, you can run in this:

- `npm start` : it runs the app in a live electron process
- `npm package` : it creates an **electron-jar 1.0.0.exe** app in **electron-jar-package/distribution**

For both, you can read the backend log into the electron window using **F1 keyword**.

> The default port used in spring boot, called from the electron app, is **8081**. You can change it in the **electron.app.config.json**
