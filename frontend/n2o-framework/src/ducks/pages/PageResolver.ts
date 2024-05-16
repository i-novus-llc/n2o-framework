// eslint-disable-next-line @typescript-eslint/no-extraneous-class
class PageResolver {
    /**
     * Значение поумолчанию
     * @return {{
     *  metadata: {},
     *  disabled: boolean,
     *  loading: boolean,
     *  error: boolean,
     *  status: null
     * }}
     */
    static get defaultState() {
        return ({
            metadata: {},
            loading: false,
            error: false,
            disabled: false,
            status: null,
            pageUrl: null,
        })
    }
}

export default PageResolver
