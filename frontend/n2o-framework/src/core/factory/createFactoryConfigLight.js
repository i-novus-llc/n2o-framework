import merge from 'deepmerge'

import InputText from '../../components/controls/InputText/InputText'
import Checkbox from '../../components/controls/Checkbox/CheckboxControl'
import DatePicker from '../../components/controls/DatePicker/DatePicker'
import DateInterval from '../../components/controls/DatePicker/DateInterval'
import PasswordInput from '../../components/controls/PasswordInput/PasswordInput'
import CheckboxGroup from '../../components/controls/CheckboxGroup/CheckboxGroupControl'
import Html from '../../components/controls/Html/Html'
import InputMask from '../../components/controls/InputMask/InputMask'
import InputNumber from '../../components/controls/InputNumber/InputNumber'
import InputSelectContainer from '../../components/controls/InputSelect/InputSelectContainer'
import InputSelectTreeContainer from '../../components/controls/InputSelectTree/InputSelectTreeContainer'
import N2OSelectContainer from '../../components/controls/N2OSelect/N2OSelectContainer'
import RadioGroupControl from '../../components/controls/RadioGroup/RadioGroupControl'
import SelectContainer from '../../components/controls/Select/SelectContainer'
import OutPutText from '../../components/controls/Output/OutputText'
import TextArea from '../../components/controls/TextArea/TextArea'
import InputHidden from '../../components/controls/InputHidden/InputHidden'
import ButtonUploader from '../../components/controls/FileUploader/ButtonUploader'
import DropZone from '../../components/controls/FileUploader/DropZone'
import Switch from '../../components/controls/Switch/Switch'
import InputMoney from '../../components/controls/InputMoney/InputMoney'
import SliderContainer from '../../components/controls/Slider/Slider'
import Rating from '../../components/controls/Rating/Rating'
import Pills from '../../components/controls/Pills/Pills'
import AutoComplete from '../../components/controls/AutoComplete/AutoComplete'
import HtmlWidget from '../../components/widgets/Html/HtmlWidget'
import FormWidget from '../../components/widgets/Form/FormWidget'
import EditForm from '../../components/widgets/Form/FormWidget'
import WireframeWidget from '../../components/widgets/Wireframe/WireframeWidget'
import AdvancedTableWidget from '../../components/widgets/AdvancedTable/AdvancedTableWidget'
import TreeWidget from '../../components/widgets/Tree/TreeWidget'
import ListWidget from '../../components/widgets/List/ListWidget'
import regions from '../../components/regions'
import pages from '../../components/pages'
import headers from '../../components/widgets/Table/headers'
import cells from '../../components/widgets/Table/cells'
import fieldsets from '../../components/widgets/Form/fieldsets'
import fields from '../../components/widgets/Form/fields'
import actions from '../../impl/actions'
import snippets from '../../components/snippets'
import buttons from '../../components/buttons'

export const factoriesLight = {
    controls: {
        InputText,
        OutputText: OutPutText,
        Checkbox,
        DatePicker,
        DateInterval,
        PasswordInput,
        CheckboxGroup,
        Html,
        InputMask,
        InputNumber,
        InputSelect: InputSelectContainer,
        InputSelectTree: InputSelectTreeContainer,
        N2OSelect: N2OSelectContainer,
        RadioGroup: RadioGroupControl,
        Select: SelectContainer,
        TextArea,
        InputHidden,
        ButtonUploader,
        DropZone,
        Switch,
        InputMoney,
        Slider: SliderContainer,
        Rating,
        Pills,
        AutoComplete,
    },
    widgets: {
        HtmlWidget,
        FormWidget,
        EditForm,
        WireframeWidget,
        AdvancedTableWidget,
        ListWidget,
        TreeWidget,
    },
    regions,
    pages,
    headers,
    cells,
    fieldsets,
    fields,
    actions,
    snippets,
    buttons,
}

export default function createFactoryConfigLight(customConfig) {
    return merge(factoriesLight, customConfig)
}
