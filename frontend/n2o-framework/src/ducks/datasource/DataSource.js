export class DataSource {
    static get defaultState() {
        return ({
            provider: {},
            validation: {},
            components: [],
            dependencies: [],
            size: 0,
            page: 1,
            loading: false,
            sorting: {},
            submit: {},
        })
    }
}
