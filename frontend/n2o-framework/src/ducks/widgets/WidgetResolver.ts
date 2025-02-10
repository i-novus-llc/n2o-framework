// eslint-disable-next-line @typescript-eslint/no-extraneous-class
class WidgetResolver {
    static get defaultState() {
        return ({
            isInit: false,
            visible: true,
            fetch: 'always',
            disabled: false,
            isFilterVisible: true,
            isActive: false,
            type: null,
            /* Query props */
            /* System props */
            pageId: null,
            error: null,
            validation: {},
            // datasource: null,
        })
    }
}

export default WidgetResolver
