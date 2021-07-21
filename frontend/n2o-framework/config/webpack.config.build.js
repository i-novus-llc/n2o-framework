/**
 * Конфигурация webpack для сборки ./dist/
 */

const path = require('path')

const MiniCssExtractPlugin = require('mini-css-extract-plugin')

const sourceDir = path.resolve(__dirname, '../src')
const distDir = path.resolve(__dirname, '../dist')
const plugins = []
const isProduction = process.env.NODE_ENV === 'production'
// const isDevelopment = !isProduction

if (isProduction) {
    plugins.push(new MiniCssExtractPlugin({
        filename: 'n2o.css',
    }))
}

module.exports = {
    context: sourceDir,
    entry: ['./sass/n2o.scss'],
    output: {
        path: distDir,
        filename: 'n2o.js',
        library: 'N2O',
        libraryTarget: 'umd',
    },
    stats: {
        colors: true,
        reasons: false,
        hash: false,
        version: false,
        timings: true,
        chunks: false,
        chunkModules: false,
        cached: false,
        cachedAssets: false,
    },
    resolve: {
        modules: [sourceDir, 'node_modules'],
        extensions: ['.js', '.jsx'],
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)?$/,
                include: [sourceDir],
                use: ['babel-loader'],
            },
            {
                test: /\.scss$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    { loader: 'css-loader', options: { sourceMap: true, importLoaders: 1 } },
                    { loader: 'sass-loader', options: { sourceMap: true } },
                ],
            },
            {
                test: /.(ttf|otf|eot|svg|woff(2)?)(\?[\da-z]+)?$/,
                use: [
                    {
                        loader: 'file-loader',
                        options: {
                            name: '[name].[ext]',
                            outputPath: 'fonts/', // where the fonts will go
                            publicPath: './fonts/', // override the default path
                        },
                    },
                ],
            },
        ],
    },
    externals: {
        react: {
            root: 'React',
            commonjs: 'react',
            commonjs2: 'react',
            amd: 'react',
        },
        'react-dom': {
            root: 'ReactDOM',
            commonjs: 'react-dom',
            commonjs2: 'react-dom',
            amd: 'react-dom',
        },
    },
    plugins,
}
