export class DataSource {
    static get defaultState() {
        return ({
            provider: {},
            validations: {},
            components: [],
            dependencies: [],
            size: 0,
            page: 1,
            loading: false,
            sorting: {},
            submit: null,
            errors: {},
        })
    }
}
