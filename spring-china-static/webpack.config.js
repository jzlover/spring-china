var path = require('path');
var WebpackOnBuildPlugin = require('on-build-webpack');
var compressor = require('node-minify');
var ProgressBarPlugin = require('progress-bar-webpack-plugin');

var config = require('./config.js');
var webpack = require('webpack');
var file = require('./lib/js/file.js');

var entrys = {};
for (var j = 0; j < config.apps.length; j++) {
    var files = file.listFiles('./' + config.apps[j] + '/');
    for (var i = 0; i < files.length; i++) {
        if (/\.js$/.test(files[i])) {
            var flag = files[i].replace(/\.js$/, '');
            entrys[flag] = files[i];
        }
    }
}

if ('initScript' in config) {
} else {
    config['initScript'] = 'init.js';
}

var webpackConfig = {
    plugins: [
        new ProgressBarPlugin(),
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery'
        }),
        /*
        new webpack.optimize.UglifyJsPlugin({
            sourceMap: false,
            compress: {
                warnings: false
            },
            comments: false,
        }),
        */
        new webpack.optimize.CommonsChunkPlugin(config['initScript']),
        new WebpackOnBuildPlugin(function (stats) {
            for (var file in stats.compilation.assets) {
                if (/\.js$/.test(file)) {
                    // console.log('minify file : ' + file);
                    // compressor.minify({
                    //     compressor: 'gcc',
                    //     input: config.dist + '/' + file,
                    //     output: config.dist + '/' + file,
                    //     buffer: 1000 * 1024,
                    //     callback: function (err, min) {
                    //     }
                    // });
                }
            }
        }),
    ],
    entry: entrys,
    output: {
        path: config.dist,
        filename: '[name].js',
        publicPath: config.cdn + '/'
    },
    module: {
        loaders: [
            {test: /\.js$/, loader: 'jsx-loader?harmony'},
            {test: /\.css$/, loader: 'style-loader!css-loader'},
            {test: /\.less$/, loader: 'style!css!less'},
            {test: /\.html$/, loader: 'html-loader'},
            {test: /\.(png|jpg|gif|jpeg|)$/, loader: 'url-loader?limit=10000&name=sprites/[hash].[ext]'},
            {test: require.resolve('./lib/js/jquery'), loader: 'expose?jQuery!expose?$'},
            {
                test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url-loader?limit=10000&mimetype=application/font-woff&name=./assets/fonts/[hash].[ext]"
            },
            {
                test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url-loader?limit=10000&mimetype=application/font-woff&name=./assets/fonts/[hash].[ext]"
            },
            {
                test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url-loader?limit=10000&mimetype=application/octet-stream&name=./assets/fonts/[hash].[ext]"
            },
            {
                test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file-loader"
            },
            {
                test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url-loader?limit=10000&mimetype=image/svg+xml&name=./assets/fonts/[hash].[ext]"
            }
        ]
    },
    resolve: {
        root: __dirname,
        extensions: ['', '.js', '.css'],
        alias: {
            jquery: "lib/js/jquery",
            basePC: "lib/js/basePC"
        }
    }
};

module.exports = webpackConfig;