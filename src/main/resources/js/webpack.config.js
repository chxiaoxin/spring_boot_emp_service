var path = require('path');

module.exports = {
    entry: './index.js',
    devtool: 'sourcemaps',
    cache: true,
    output: {
        path: __dirname,
        filename: '../static/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            }
        ]
    }
};
