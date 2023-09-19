module.exports = {
    plugins: [],
    style: {
        sass: {
            loaderOptions: (sassLoaderOptions) => {
                sassLoaderOptions.sassOptions = {
                    quietDeps: true
                };
                return sassLoaderOptions;
            },
        },
    },
}
