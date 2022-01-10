export class DataSource {
    static get defaultState() {
        return ({
            provider: {},
            validations: {},
            widgets: [],
            dependencies: [],
            size: 0,
            page: 1,
            loading: false,
            sorting: {},
            submit: {},
            errors: {},
        })
    }
}
