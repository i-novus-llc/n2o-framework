// eslint-disable-next-line @typescript-eslint/no-extraneous-class
class PageResolver {
    static get defaultState() {
        return ({
            id: '',
            metadata: {},
            loading: false,
            error: false,
            disabled: false,
            pageUrl: '/',
            rootPage: false,
            scroll: false,
        })
    }
}

export default PageResolver
