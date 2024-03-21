package com.merit.openCsv;

import com.merit.domain.*;
import com.merit.dto.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OpenCsv {

    // * main 함수에서 실험하는 용도
    public static void readDataFromCsv(String filePath, int maxLines) throws IOException, CsvValidationException {
        int lineCount = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null && lineCount < maxLines) {
                if (lineCount == 0) {
                    lineCount++;
                    continue;
                }
                for (int i = 0; i < nextLine.length; i++) {
                    System.out.println(i + " " + nextLine[i]);
                }
                System.out.println();
                lineCount++;
            }
        }
    }

    public static List<ProjectContractorDto> readProjectContractorDataFromCsv(String filePath, int maxLines) throws IOException, CsvValidationException {
        List<ProjectContractorDto> projectContractorDtos = new ArrayList<>();
        int lineCount = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null & lineCount < maxLines) {
                if (lineCount == 0) { // 첫 번째 줄은 헤더이므로 스킵
                    lineCount++;
                    continue;
                }
                // CSV 파일의 각 행에서 필요한 정보를 추출하여 ProjectContractorDto 객체를 생성합니다.
                ProjectContractorDto projectContractorDto = ProjectContractorDto.builder()
                        .id(Long.parseLong(nextLine[0]))
                        .status(ProjectContractorStatus.valueOf(nextLine[1].toUpperCase()))
                        .comment(nextLine[2])
                        .rateType(nextLine[3])
                        .expectedRate(Float.parseFloat(nextLine[4]))
                        .expectedHoursPerWeek(Integer.parseInt(nextLine[5]))
                        .expectedPayCurrency(nextLine[6])
                        .expectedExchangeRate(Float.parseFloat(nextLine[7]))
                        .applicationDate(LocalDate.parse(nextLine[8]))
                        .build();

                projectContractorDtos.add(projectContractorDto);
                lineCount++;
            }
        }
        return projectContractorDtos;
    }


    public static List<ContractorDto> readContractorDataFromCsv(String filePath, int maxLines) throws IOException, CsvValidationException {
        List<ContractorDto> contractorDtos = new ArrayList<>();
        int lineCount = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null && lineCount < maxLines) { // 읽은 줄의 수가 maxLines보다 작을 때만 처리
                if (lineCount == 0) { // 첫 번째 줄은 헤더이므로 스킵
                    lineCount++;
                    continue;
                }
                // CSV 파일의 각 행에서 필요한 정보를 추출하여 ContractorDto 객체를 생성합니다.
                ContractorDto contractorDto = ContractorDto.builder()
                        .id(Long.parseLong(nextLine[0]))
                        .name(nextLine[1])
                        .contractorEmail(nextLine[2])
                        .website(nextLine[3])
                        .status(ContractorStatus.valueOf(nextLine[4].toUpperCase()))
                        .address(parseAddress(nextLine[5]))
                        .contactNumber(nextLine[6])
                        .experience(Integer.parseInt(nextLine[7]))
                        .expectedPay(Integer.parseInt(nextLine[8]))
                        .expectedPayCurrency(nextLine[9])
                        .resume(new PdfDocument(nextLine[10]))
                        .avatar(new Image(nextLine[11]))
                        .createdOn(LocalDate.parse(nextLine[12]))
                        .modifiedOn(LocalDate.parse(nextLine[13]))
                        .build();
                contractorDtos.add(contractorDto);
                lineCount++;
            }
        }
        return contractorDtos;
    }

    public static List<ProjectDto> readProjectDataFromCsv(String filePath, int maxLines) throws IOException, CsvValidationException {
        List<ProjectDto> projectDtos = new ArrayList<>();
        int lineCount = 0; // 읽은 줄의 수를 저장하기 위한 변수 추가
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null && lineCount < maxLines) { // 읽은 줄의 수가 maxLines보다 작을 때만 처리
                if (lineCount == 0) { // 첫 번째 줄은 헤더이므로 스킵
                    lineCount++;
                    continue;
                }
                // CSV 파일의 각 행에서 필요한 정보를 추출하여 ProjectDto 객체를 생성합니다.
                ProjectDto projectDto = ProjectDto.builder()
                        .id(Long.parseLong(nextLine[0]))
                        .name(nextLine[1])
                        .projectDescription(nextLine[2])
                        .role(nextLine[3])
                        .minExpReqd(Integer.parseInt(nextLine[4]))
                        .maxExpReqd(Integer.parseInt(nextLine[5]))
                        .status(ProjectStatus.valueOf(nextLine[6].toUpperCase()))
                        .createdBy(nextLine[7])
                        .companyId(Long.parseLong(nextLine[8]))
                        .createdOn(LocalDate.parse(nextLine[9]))
                        .modifiedOn(LocalDate.parse(nextLine[10]))
                        .salaryRange(Integer.parseInt(nextLine[11]))
                        .build();
                projectDtos.add(projectDto);
                lineCount++;
            }
        }
        return projectDtos;
    }

    public static List<SkillDto> readSkillDataFromCsv(String filePath, int maxLines) throws IOException, CsvValidationException {
        List<SkillDto> skillDtos = new ArrayList<>();
        int lineCount = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null && lineCount < maxLines) { // 읽은 줄의 수가 maxLines보다 작을 때만 처리
                if (lineCount == 0) { // 첫 번째 줄은 헤더이므로 스킵
                    lineCount++;
                    continue;
                }
                // CSV 파일의 각 행에서 필요한 정보를 추출하여 SkillDto 객체를 생성합니다.
                SkillDto skillDto = SkillDto.builder()
                        .id(Long.parseLong(nextLine[0]))
                        .name(nextLine[1])
                        .skillsDescription(nextLine[2])
                        .build();

                skillDtos.add(skillDto);
                lineCount++;
            }
        }
        return skillDtos;
    }

    public static CompanyDto readCompanyDataFromCsv(String filePath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            reader.skip(1); // 첫 번째 줄은 칼럼명이므로 스킵합니다.
            if ((nextLine = reader.readNext()) != null) {
                // CSV 파일의 각 열에서 필요한 정보를 추출하여 CompanyDto 객체를 생성합니다.
                return CompanyDto.builder()
                        .id(Long.parseLong(nextLine[0]))
                        .name(nextLine[1])
                        .email(nextLine[2])
                        .website(nextLine[3])
                        .contactNumber(nextLine[4])
                        .address(parseAddress(nextLine[5]))
                        .about(nextLine[6])
                        .createdOn(LocalDate.parse(nextLine[7]))
                        .status(CompanyStatus.valueOf(nextLine[8].toUpperCase()))
                        .build();
            }
        }
        return null; // 파일이 비어있는 경우 또는 회사 정보가 없는 경우 null을 반환합니다.
    }


    public static Address parseAddress(String addressString) {
        if (addressString == null || addressString.isEmpty()) {
            return null;
        }

        // CSV 파일에서 주소는 쉼표로 구분된 문자열이므로 쉼표를 기준으로 문자열을 분리하여 Address 객체 생성
        String[] addressParts = addressString.split(",");
        if (addressParts.length < 3) {
            // 충분한 정보가 없는 경우 null을 반환하거나 예외를 처리할 수 있습니다.
            return null;
        }

        String city = addressParts[0].trim();
        String street = addressParts[1].trim();
        String zipcode = addressParts[2].trim();
        return new Address(city, street, zipcode);
    }

    public static void main(String[] args) throws IOException, CsvValidationException {
        readDataFromCsv("merit/src/test/resources/project.csv", 5);
    }
}
