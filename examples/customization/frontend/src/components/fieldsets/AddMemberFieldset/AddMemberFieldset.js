import React from "react";
import { isEqual, map } from "lodash";
import { Row, Col, Button } from "reactstrap";
import ReduxField from "n2o/lib/components/widgets/Form/ReduxField";
import PropTypes from "prop-types";

class AddMemberFieldset extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      rows: props.rows
    };

    this.template = props.rows;

    this.addMember = this.addMember.bind(this);
    this.createNewRow = this.createNewRow.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (!isEqual(prevProps.rows, this.props.rows)) {
      this.setState({ rows: this.props.rows });
    }
  }

  createNewRow() {
    return map(this.template, ({ cols }) => ({
      cols: map(cols, ({ fields }) => ({
        fields: map(fields, item => ({
          ...item,
          id: item.id + this.state.rows.length
        }))
      }))
    }));
  }

  addMember() {
    const rows = this.state.rows;
    const template = this.createNewRow();
    rows.push(template[0]);
    this.setState({
      rows
    });
  }

  renderFields(rows) {
    return map(rows, ({ cols }) => (
      <Row>
        {map(cols, ({ fields }) => (
          <Col md={6}>
            {map(fields, item => (
              <ReduxField {...item} />
            ))}
          </Col>
        ))}
      </Row>
    ));
  }

  render() {
    return (
      <div
        style={{
          padding: "40px"
        }}
      >
        <div>
          <div
            style={{
              display: "flex"
            }}
          >
            <div style={{ flexGrow: 1 }}>
              {this.renderFields(this.state.rows)}
            </div>
            <div
              style={{
                alignSelf: "flex-end",
                marginLeft: 20,
                marginBottom: 25
              }}
            >
              <Button
                onClick={this.addMember}
                size={"sm"}
                style={{ width: 38, height: 38 }}
              >
                <i className="fa fa-plus-circle" />
              </Button>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

AddMemberFieldset.contextTypes = {
  resolveProps: PropTypes.func
};

export default AddMemberFieldset;
