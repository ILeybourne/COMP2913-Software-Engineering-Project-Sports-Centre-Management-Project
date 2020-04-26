// webpack.config.js
import "@html-webpack-plugin"
import {extractColor} from "vuetify/lib/components/VColorPicker/util";

module.exports = {
  module: {
    rules: [
      {
        test: /\.(png|jpg|gif)$/i,
        use: [
          {
            loader: "file-loader",
            options: {
              name: "[name].[ext]",
              outputPath: "src/assets/",
              publicPath: "src/assets/"
            }
          }
        ]
      },
        {

        }
    ]
  },
    plugins: [
        extractPlugin,
        new html-webpack
    ]

};
