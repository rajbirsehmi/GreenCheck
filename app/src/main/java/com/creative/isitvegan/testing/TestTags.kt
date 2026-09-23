package com.creative.isitvegan.testing

object TestTags {
    object V2 {
        object Components {
            object Privacy {
                const val SHEET_CONTAINER = "transparency_sheet_container"
                const val TITLE = "transparency_title"
                const val ITEM_FREE = "transparency_item_free"
                const val ITEM_LOCAL = "transparency_item_local"
                const val ITEM_LIMITS = "transparency_item_limits"
                const val ITEM_OPEN_SOURCE = "transparency_item_open_source"
                const val ITEM_LICENSING = "transparency_item_licensing"
                const val BTN_AUDIT_CODE = "button_audit_code"
                const val BTN_AUDIT_CODE_ICON = "button_audit_code_icon"
                const val BTN_AUDIT_CODE_TEXT = "button_audit_code_text"
                const val BTN_ODBL_LICENSE = "button_odbl_license"
                const val BTN_ODBL_LICENSE_TEXT = "button_odbl_license_text"
                fun itemIconContainer(title: String) = "transparency_item_icon_container_${title.lowercase().replace(" ", "_")}"
                fun itemIcon(title: String) = "transparency_item_icon_${title.lowercase().replace(" ", "_")}"
                fun itemTitle(title: String) = "transparency_item_title_${title.lowercase().replace(" ", "_")}"
                fun itemDesc(title: String) = "transparency_item_desc_${title.lowercase().replace(" ", "_")}"
            }
            object ProductItem {
                fun container(barcode: String) = "product_item_$barcode"
                const val IMAGE = "product_image"
                const val NAME = "product_name"
                const val BRAND = "product_brand_name"
                fun statusDot(barcode: String) = "product_status_dot_$barcode"
                fun statusText(barcode: String) = "product_status_text_$barcode"
                fun timestamp(barcode: String) = "product_timestamp_$barcode"
            }
        }

        object Scaffolding {
            const val BOTTOM_NAV_BAR = "bottom_navigation_bar"
            fun navItem(route: String) = "nav_item_$route"
            const val SHEET_APP_USAGE = "sheet_app_usage"
            const val USAGE_SHEET_TITLE = "usage_sheet_title"
            const val USAGE_CARD = "usage_card"
            const val USAGE_RESET_TIME = "usage_reset_time"
            const val BTN_OPEN_FOOD_FACTS = "button_open_food_facts_link"
            const val LINK_OFF_TEXT = "link_off_text"
            const val LINK_ODBL_TEXT = "link_odbl_text"
            const val BTN_DATA_TRANSPARENCY = "button_data_transparency"
            const val BTN_DATA_TRANSPARENCY_ICON = "button_data_transparency_icon"
            const val BTN_CLOSE_USAGE_SHEET = "button_close_usage_sheet"
            const val SHEET_TRANSPARENCY_IN_ACCOUNT = "sheet_transparency_in_account"
            fun usageRow(label: String) = "usage_row_${label.lowercase().replace(" ", "_")}"
            fun usageLabel(label: String) = "usage_label_${label.lowercase().replace(" ", "_")}"
            fun usageValue(label: String) = "usage_value_${label.lowercase().replace(" ", "_")}"
            const val TOP_BAR_TITLE = "top_bar_title"
            const val BTN_OPEN_INFO = "button_open_info"
            const val BTN_OPEN_INFO_ICON = "button_open_info_icon"
            const val TOP_APP_BAR = "top_app_bar"
        }

        object Home {
            const val HERO_SECTION = "home_hero_section"
            const val LOGO = "home_logo"
            const val TITLE = "home_title"
            const val SUBTITLE = "home_subtitle"
            const val QUICK_ACTIONS_TITLE = "home_quick_actions_title"
            const val ACTIONS_ROW_1 = "home_actions_row_1"
            const val CARD_SCANNER = "card_scanner"
            const val CARD_SEARCH = "card_search"
            const val ACTIONS_ROW_2 = "home_actions_row_2"
            const val CARD_MANUAL = "card_manual"
            const val CARD_HISTORY = "card_history"
            const val SECTION_HOW_IT_WORKS = "section_how_it_works"
            const val SECTION_DISCLAIMER = "section_disclaimer"
            fun infoSectionTitle(title: String) = "info_section_title_${title.lowercase().replace(" ", "_")}"
            fun infoSectionContent(title: String) = "info_section_content_${title.lowercase().replace(" ", "_")}"
        }

        object EmptyHistory {
            const val SCREEN = "empty_history_screen"
            const val LOGO_CONTAINER = "empty_history_logo_container"
            const val LOGO_ICON = "empty_history_logo_icon"
            const val TITLE = "empty_history_title"
            const val SUBTITLE = "empty_history_subtitle"
        }

        object Error {
            const val SCREEN = "error_screen"
            const val CONTENT = "error_content"
            const val ICON_CONTAINER = "error_icon_container"
            const val ICON = "error_icon"
            const val TITLE = "error_title"
            const val DESCRIPTION = "error_description"
            const val BTN_RETURN_HOME = "button_error_return_home"
            const val BTN_RETURN_HOME_TEXT = "button_error_return_home_text"
            const val BTN_RETRY = "button_error_retry"
            const val BTN_RETRY_ICON = "button_error_retry_icon"
            const val BTN_RETRY_TEXT = "button_error_retry_text"
        }

        object History {
            const val SCREEN = "history_screen"
            const val LIST = "history_list"
            const val CLEAR_CONTAINER = "history_clear_container"
            const val BTN_CLEAR = "button_clear_history"
            const val BTN_CLEAR_TEXT = "button_clear_history_text"
        }

        object LoadingProduct {
            const val SCREEN = "loading_product_screen"
            const val CONTENT = "loading_product_content"
            const val INDICATOR = "loading_product_indicator"
            const val TITLE = "loading_product_title"
            const val SUBTITLE = "loading_product_subtitle"
        }

        object ManualEntry {
            const val SCREEN = "manual_entry_screen"
            const val LOGO_CONTAINER = "manual_entry_logo_container"
            const val LOGO_ICON = "manual_entry_logo_icon"
            const val TITLE = "manual_entry_title"
            const val SUBTITLE = "manual_entry_subtitle"
            const val INPUT_CARD = "manual_entry_input_card"
            const val BARCODE_FIELD = "manual_entry_barcode_field"
            const val DIGIT_COUNT = "manual_entry_digit_count"
            const val FIELD_ICON = "manual_entry_field_icon"
            const val BTN_IDENTIFY = "button_identify_product"
            const val BTN_IDENTIFY_ICON = "button_identify_product_icon"
            const val BTN_IDENTIFY_TEXT = "button_identify_product_text"
            const val QUOTA_CONTAINER = "manual_entry_quota_container"
            const val QUOTA_TEXT = "manual_entry_quota_text"
        }

        object Product {
            const val LOADING = "product_loading"
            const val CONTENT_LIST = "product_content_list"
            const val BTN_CLOSE = "button_close_product_screen"
            const val BTN_CLOSE_TEXT = "button_close_product_screen_text"
            const val HERO_SECTION = "product_hero_section"
            const val IMAGE_CONTAINER = "product_image_container"
            const val IMAGE = "product_image"
            const val BRAND_OVERLAY = "product_brand_overlay"
            const val BRAND_NAME = "product_brand_name"
            const val TITLE_SECTION = "product_title_section"
            const val NAME = "product_name"
            const val BARCODE = "product_barcode"
            const val STATUS_BANNER = "product_status_banner"
            const val STATUS_ICON_CONTAINER = "product_status_icon_container"
            const val STATUS_DOT = "product_status_dot"
            const val STATUS_TITLE = "product_status_title"
            const val STATUS_DESCRIPTION = "product_status_description"
            const val DETAILS_SECTION = "product_details_section"
            const val DETAILS_CARD = "product_details_card"
            fun detailRow(label: String) = "detail_row_${label.lowercase()}"
            fun detailLabel(label: String) = "detail_label_${label.lowercase()}"
            fun detailValue(label: String) = "detail_value_${label.lowercase()}"
            const val INGREDIENTS_ANALYSIS_SECTION = "ingredients_analysis_section"
            fun analysisCard(tagSuffix: String) = "analysis_card_$tagSuffix"
            fun analysisCardDot(tagSuffix: String) = "analysis_card_dot_$tagSuffix"
            fun analysisCardTitle(tagSuffix: String) = "analysis_card_title_$tagSuffix"
            fun analysisCardItems(tagSuffix: String) = "analysis_card_items_$tagSuffix"
            fun analysisCardItem(tagSuffix: String, index: Int) = "analysis_card_item_${tagSuffix}_$index"
            fun analysisCardItemText(tagSuffix: String, index: Int) = "analysis_card_item_text_${tagSuffix}_$index"
            fun analysisCardEmpty(tagSuffix: String) = "analysis_card_empty_$tagSuffix"
            const val ALL_INGREDIENTS_SECTION = "all_ingredients_section"
            const val ALL_INGREDIENTS_CARD = "all_ingredients_card"
            const val ALL_INGREDIENTS_LIST = "all_ingredients_list"
            fun allIngredientsItem(index: Int) = "all_ingredients_item_$index"
            fun allIngredientsItemText(index: Int) = "all_ingredients_item_text_$index"
            fun sectionTitle(title: String) = "section_title_${title.lowercase().replace(" ", "_")}"
            const val ERROR_STATE_CONTAINER = "error_state_container"
            const val ERROR_STATE_ICON = "error_state_icon"
            const val ERROR_STATE_TITLE = "error_state_title"
            const val ERROR_STATE_MESSAGE = "error_state_message"
            const val BTN_ERROR_RETRY = "button_error_retry"
            const val BTN_ERROR_RETRY_ICON = "button_error_retry_icon"
            const val BTN_ERROR_RETRY_TEXT = "button_error_retry_text"
        }

        object QuotaExhausted {
            const val SCREEN = "quota_exhausted_screen"
            const val CONTENT = "quota_exhausted_content"
            const val ICON_CONTAINER = "quota_exhausted_icon_container"
            const val ICON = "quota_exhausted_icon"
            const val TITLE = "quota_exhausted_title"
            const val DESCRIPTION = "quota_exhausted_description"
            const val BTN_UNDERSTOOD = "button_quota_understood"
            const val BTN_UNDERSTOOD_TEXT = "button_quota_understood_text"
        }

        object Scanner {
            const val SCREEN_CONTAINER = "scanner_screen_container"
            const val CAMERA_PREVIEW = "camera_preview"
            const val INSTRUCTIONS_OVERLAY = "scanner_instructions_overlay"
            const val ICON = "scanner_icon"
            const val STATUS_TEXT = "scanner_status_text"
            const val BTN_TOGGLE_VIBRATION = "button_toggle_vibration"
            const val BTN_TOGGLE_VIBRATION_ICON = "button_toggle_vibration_icon"
            const val QUOTA_OVERLAY = "scanner_quota_overlay"
            const val QUOTA_TEXT = "scanner_quota_text"
            const val VIEWFINDER_CONTAINER = "scanner_viewfinder_container"
            const val VIEWFINDER_WINDOW = "scanner_viewfinder_window"
        }

        object Search {
            const val LIST = "search_screen_list"
            const val HEADER_SECTION = "search_header_section"
            const val LOGO_CONTAINER = "search_logo_container"
            const val LOGO_ICON = "search_logo_icon"
            const val TITLE = "search_title"
            const val SUBTITLE = "search_subtitle"
            const val MODE_SELECTION_ROW = "search_mode_selection_row"
            const val MODE_PRODUCTS = "search_mode_products"
            const val MODE_INGREDIENTS = "search_mode_ingredients"
            const val INPUT_CARD = "search_input_card"
            const val TEXT_FIELD = "search_text_field"
            const val FIELD_ICON = "search_field_icon"
            const val BTN_INITIALIZE = "button_initialize_search"
            const val LOADING_INDICATOR = "search_loading_indicator"
            const val BTN_INITIALIZE_ICON = "button_initialize_search_icon"
            const val BTN_INITIALIZE_TEXT = "button_initialize_search_text"
            const val QUOTA_CONTAINER = "search_quota_container"
            const val QUOTA_TEXT = "search_quota_text"
            const val ERROR_TEXT = "search_error_text"
            const val NO_RESULTS_TEXT = "search_no_results_text"
            fun selectionCard(title: String) = "search_selection_card_${title.lowercase()}"
            fun selectionCardContent(title: String) = "search_selection_card_content_${title.lowercase()}"
            fun selectionCardIcon(title: String) = "search_selection_card_icon_${title.lowercase()}"
            fun selectionCardTitle(title: String) = "search_selection_card_title_${title.lowercase()}"
            fun selectionCardSubtitle(title: String) = "search_selection_card_subtitle_${title.lowercase()}"
        }

        object Welcome {
            const val LOGO = "welcome_logo"
            const val TITLE = "welcome_title"
            const val SUBTITLE = "welcome_subtitle"
            const val BOTTOM_SECTION = "welcome_bottom_section"
            const val DESCRIPTION = "welcome_description"
            const val BTN_GET_STARTED = "button_get_started"
            const val BTN_GET_STARTED_TEXT = "button_get_started_text"
            const val BTN_GET_STARTED_ICON = "button_get_started_icon"
            const val BTN_HOW_WE_HANDLE_DATA = "button_how_we_handle_data"
            const val BTN_HOW_WE_HANDLE_DATA_TEXT = "button_how_we_handle_data_text"
            const val SHEET_TRANSPARENCY = "sheet_transparency"
        }
    }
}
