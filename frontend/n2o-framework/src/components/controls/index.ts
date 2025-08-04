import { CheckboxControl as Checkbox } from '@i-novus/n2o-components/lib/inputs/Checkbox/CheckboxControl'
import { DateInterval } from '@i-novus/n2o-components/lib/inputs/DatePicker/DateInterval'
import { DatePicker } from '@i-novus/n2o-components/lib/inputs/DatePicker/DatePicker'
import { InputText } from '@i-novus/n2o-components/lib/inputs/InputText'
import { InputMask } from '@i-novus/n2o-components/lib/inputs/InputMask'
import { InputMoney } from '@i-novus/n2o-components/lib/inputs/InputMoney'
import { InputPassword as PasswordInput } from '@i-novus/n2o-components/lib/inputs/InputPassword'
import { NumberPicker } from '@i-novus/n2o-components/lib/inputs/NumberPicker'
import { InputNumber } from '@i-novus/n2o-components/lib/inputs/InputNumber'
import { OutputList } from '@i-novus/n2o-components/lib/display/OutputList'
import { OutputText } from '@i-novus/n2o-components/lib/display/OutputText'
import { ProgressControl } from '@i-novus/n2o-components/lib/display/ProgressControl'
import { Slider } from '@i-novus/n2o-components/lib/display/Slider'
import { Switch } from '@i-novus/n2o-components/lib/inputs/Switch'
import { TextArea } from '@i-novus/n2o-components/lib/inputs/TextArea'
import { TimePicker } from '@i-novus/n2o-components/lib/inputs/TimePicker'

import { defineAsync } from '../../core/factory/defineAsync'

import { Html } from './Html/Html'
import { InputHidden } from './InputHidden/InputHidden'
import { Rating } from './Rating/Rating'
import { InputSelectTreeContainer } from './InputSelectTree/InputSelectTreeContainer'
import { N2OSelectContainer } from './N2OSelect/N2OSelectContainer'
import CheckboxGroup from './CheckboxGroup/CheckboxGroup'
import InputSelectContainer from './InputSelect/InputSelectContainer'
import RadioGroup from './RadioGroup/RadioGroup'
import ButtonUploader from './FileUploader/ButtonUploader'
import DropZone from './FileUploader/DropZone'
import AutoComplete from './AutoComplete/AutoComplete'
import ImageUploader from './ImageUploader/ImageUploader'

export default {
    InputText,
    OutputText,
    Checkbox,
    DatePicker,
    DateInterval,
    PasswordInput,
    CheckboxGroup,
    CodeEditor: defineAsync(() => import('@i-novus/n2o-components/lib/inputs/CodeEditor')
        .then(({ CodeEditor }) => CodeEditor)),
    CodeViewer: defineAsync(() => import('@i-novus/n2o-components/lib/display/CodeViewer')
        .then(({ CodeViewer }) => CodeViewer)),
    Html,
    InputMask,
    InputNumber,
    InputSelect: InputSelectContainer,
    InputSelectTree: InputSelectTreeContainer,
    N2OSelect: N2OSelectContainer,
    RadioGroup,
    TextEditor: defineAsync(() => import('@i-novus/n2o-components/lib/inputs/TextEditor')
        .then(({ TextEditor }) => TextEditor)),
    TextArea,
    InputHidden,
    ButtonUploader,
    DropZone,
    Switch,
    InputMoney,
    Slider,
    Rating,
    AutoComplete,
    ProgressControl,
    ImageUploader,
    OutputList,
    NumberPicker,
    TimePicker,
}
