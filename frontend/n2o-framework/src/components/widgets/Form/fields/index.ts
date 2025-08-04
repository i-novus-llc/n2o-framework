import { defineAsync } from '../../../../core/factory/defineAsync'

import { StandardField } from './StandardField/StandardField'
import { TextField } from './TextField/TextField'
import { RangeField } from './RangeField/RangeField'
import { AlertField } from './AlertField/AlertField'
import { ButtonField } from './ButtonField/ButtonField'
import { ImageField } from './ImageField/ImageField'
import { HtmlField as Html } from './HtmlField/HtmlField'
import { FilterSearchButton } from './FilterButtons/FilterSearchButton/FilterSearchButton'
import { FilterClearButton } from './FilterButtons/FilterClearButton/FilterClearButton'
import { FilterButtons } from './FilterButtons/FilterButtons'

export default {
    StandardField,
    TextField,
    RangeField,
    AlertField,
    ButtonField,
    ImageField,
    Html,
    MarkdownField: defineAsync(() => import('./MarkdownField/MarkdownField')
        .then(({ MarkdownField }) => MarkdownField)),
    FilterSearchButton,
    FilterClearButton,
    FilterButtons,
}
