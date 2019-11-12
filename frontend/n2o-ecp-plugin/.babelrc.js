const env = process.env.NODE_ENV;

module.exports = {
    presets: [
        env === 'commonjs'
            ? '@babel/preset-env'
            : [
                '@babel/preset-env',
                {
                    modules: false,
                },
            ],
        '@babel/preset-react',
    ],
    plugins: [
        '@babel/plugin-transform-runtime',
        '@babel/plugin-proposal-class-properties',
    ],
};
