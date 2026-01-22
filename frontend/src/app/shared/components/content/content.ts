export interface Content {
  column_header: ColumnHeader;
}

export interface ColumnHeader {
  name: string;
  toggle_group: ToggleGroup[];
}

export interface ToggleGroup {
  aria_label: string;
  name: string;
  possibility: Possibility[];
  selected_value: string;
}

export interface Possibility {
  value: string;
  name: string;
}
