import { ModelPrefix } from "../../core/datasource/const"

export const COPY = 'n2o/models/COPY'

export const ALL_PREFIXES: ModelPrefix[] = [
    ModelPrefix.source, ModelPrefix.edit,
    ModelPrefix.filter, ModelPrefix.selected,
    ModelPrefix.active
]
